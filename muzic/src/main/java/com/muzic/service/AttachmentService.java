package com.muzic.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.muzic.dao.AttachmentDao;
import com.muzic.dto.AttachmentDto;
import com.muzic.error.DataPersistenceException;
import com.muzic.error.TargetNotFoundException;
import com.muzic.util.FileUtil;

@Service
public class AttachmentService {
	
	@Autowired
	private AttachmentDao attachmentDao;
	
	private File home = new File(System.getProperty("user.home")); // 사용자의 기본폴더 // 실제 파일이 아니라 경로를 보관하고 있는 인스턴스(파일처리 가능 인스턴스)
	private static final String UPLOAD_PATH =  "muzic_uploads/"; // 하드코딩 방지 및 업로드경로바꾸고 싶을때 상수필드만 바꾸게
	
	// member(profile)에서 이미지 저장할 때
	public void save(MultipartFile attach, String category, String memberId) throws IOException {
	    saveInternal(attach, category, memberId);
	}
	
	//그 외 나머지에서 이미지 저장할 때(parentNo에 pk 넣어주시면 됩니다)
	public void save(MultipartFile attach, String category, int parentNo) throws IOException {
		saveInternal(attach, category, String.valueOf(parentNo));
	}
	
	//	Transactional의 역할 (DB 작업만 롤백)
	//	try-catch 사용 이유는 파일 삭제 및 생성은 DB작업이 아니기에 따로 예외로 처리가 필요
	@Transactional
	private void saveInternal(MultipartFile attach, String category, String attachmentParent) throws IllegalStateException, IOException {
		
	    int attachmentNo = attachmentDao.sequence(); // 등록될 pk 조회
	    
	    File categoryDir = new File(home, UPLOAD_PATH + category); // 홈디렉토리/muzic_uploads/music 등
	    if (!categoryDir.exists()) { // 업로드할 폴더가 존재하지 않는다면(경로가 존재하지 않는다면)
	    	categoryDir.mkdirs(); // 폴더가 없으면 생성
	    }
	    
	    String relativePath = UPLOAD_PATH + category; // 상대경로
	   
	    String originalName = attach.getOriginalFilename();
	    // 저장할 파일 이름(중복 방지)
	    String storedName = category + "_" + attachmentNo + "_" + FileUtil.getCleanFileName(originalName); // 카테고리_attachmentNo_원본이름

	    File target = new File(categoryDir, storedName);
	    
	    try {
	        // 1. 실제 파일 저장 시도
	    	attach.transferTo(target);

	        // 2. AttachmentDto 생성 후 DB 저장
	        AttachmentDto attachmentDto = 
	        		AttachmentDto.builder()
		            .attachmentNo(attachmentNo)
		            .attachmentType(attach.getContentType())
		            .attachmentPath(relativePath)
		            .attachmentCategory(category)
		            .attachmentParent(attachmentParent)
		            .attachmentOriginalName(originalName)
		            .attachmentStoredName(storedName)
		            .attachmentSize(attach.getSize())
		            .build();

	        attachmentDao.insert(attachmentDto);
	        
	    } catch (Exception e) {
	        //DB 오류 (런타임 예외) 또는 transferTo 오류 발생 시
	 
	        // 디스크에 저장된 파일만 수동으로 삭제하여 고아 파일을 방지
	        if (target.exists()) {
	            target.delete(); // 디스크에 남아있는 파일 삭제
	        }
	        // 발생한 예외를 다시 던져 트랜잭션 롤백
	        throw new DataPersistenceException("파일 저장 또는 DB 처리에 실패했습니다. [파일 롤백됨]");
	    }
	}
	
	public ByteArrayResource load(int attachmentNo) throws IOException {
		
		// DB에서 파일 정보 찾기
		AttachmentDto attachmentDto = attachmentDao.selectOne(attachmentNo);
		if (attachmentDto == null) throw new TargetNotFoundException("존재하지 않는 파일입니다.");
		
	    // 실제 파일 삭제 대상
	    File target = buildTargetFile(attachmentDto.getAttachmentPath(), attachmentDto.getAttachmentStoredName());
		if(target.isFile() == false) throw new TargetNotFoundException("존재하지 않는 파일입니다.");
		
		// 파일 내용 읽기
		byte[] data = Files.readAllBytes(target.toPath());
		ByteArrayResource resource = new ByteArrayResource(data);
		
		return resource;
	}
	
	// 파일 삭제(DB,실제파일)
	@Transactional
	public boolean delete(int attachmentNo) {
	    
	    AttachmentDto attachmentDto = attachmentDao.selectOne(attachmentNo);
	    if(attachmentDto == null) throw new TargetNotFoundException("존재하지 않는 파일입니다.");
	    
	    // 실제 파일 삭제 대상
	    File target = buildTargetFile(attachmentDto.getAttachmentPath(), attachmentDto.getAttachmentStoredName());
	    if(target.isFile() == false) throw new TargetNotFoundException("존재하지 않는 파일입니다.");
	    
	    // 1. 실제 파일 삭제 시도
	    boolean isDeleted = target.delete();
	    
	    // 2. 파일 삭제 실패 시 처리 (DB 삭제를 막고 롤백 유도)
	    if (!isDeleted) {
	        // Windows 등에서 파일이 사용 중/잠금 상태라 삭제에 실패했을 가능성이 높음
	        // 런타임 예외를 던져 @Transactional에 의해 DB 작업(delete)이 롤백되도록 강제
	        // 이 경우 DB 레코드가 지워지지 않고 파일도 남아 일관성이 유지
	        throw new DataPersistenceException("파일이 사용 중이거나 잠겨 있어 삭제에 실패했습니다. [DB 롤백됨]");
	    }
	    
	    // 3. 파일 삭제에 성공했을 경우에만 DB 정보 삭제
	    return attachmentDao.delete(attachmentNo); 
	}
	
	private File buildTargetFile(String path, String storedName) {
		// 상수필드 파일객체 home을 기준으로 부모 폴더 경로 생성
	    File parentDir = new File(home, path);
	    // 부모 폴더와 파일 이름을 조합하여 최종 경로 생성.
	    File target = new File(parentDir, storedName);
	    return target;
	}
	
	// 계층 분리를 위해서 AttachmentDao의 메소드를 AttachmentService만 주입받아도 실행시킬 수 있게 추가
	public int getAttachmentNoByParent(int parentSeq, String category) {
	    return attachmentDao.findAttachmentNoByParent(parentSeq, category);
	}
	
	public AttachmentDto getAttachment(int attachmentNo) {
		return attachmentDao.findAttachment(attachmentNo);
	}
	
	@Transactional
	public int saveTemp(MultipartFile attach) throws IllegalStateException, IOException {
	    
	    int attactNo = attachmentDao.sequence();

	    // 업로드 폴더 설정?
	    File tempUpload = new File(home, UPLOAD_PATH + "temp");
	    if (!tempUpload.exists()) {
	    	tempUpload.mkdirs();
	    }

	    String originalName = attach.getOriginalFilename();
	    //중복방지
	    String storedName = "temp_" + attactNo + "_" + FileUtil.getCleanFileName(originalName);
	    File target = new File(tempUpload, storedName);

	    attach.transferTo(target);

	    AttachmentDto attachmentDto = AttachmentDto.builder()
	    		.attachmentNo(attactNo) //파일 번호
	            .attachmentType(attach.getContentType()) //파일 타입
	            .attachmentPath(UPLOAD_PATH + "temp") //파일 저장 상대경로
	            .attachmentCategory("temp") //파일 분류? 카테고리?..
	            .attachmentParent(null) //부모PK?
	            .attachmentOriginalName(originalName) //원본파일
	            .attachmentStoredName(storedName) //서버에 저장된 실제 파일 이름
	            .attachmentSize(attach.getSize()) //파일의 크기
	            .build();

	        attachmentDao.insert(attachmentDto);
	        
	        return attactNo; //생성한 번호 반환

	}

}
