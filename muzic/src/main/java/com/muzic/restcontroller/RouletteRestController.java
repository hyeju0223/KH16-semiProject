package com.muzic.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.muzic.aop.PreventAdminInterceptor;
import com.muzic.dao.MemberDao;
import com.muzic.dao.MemberPointLogDao;
import com.muzic.dao.RouletteDao;
import com.muzic.dao.RouletteLogDao;
import com.muzic.dto.MemberPointLogDto;
import com.muzic.dto.RouletteDto;
import com.muzic.dto.RouletteLogDto;

@CrossOrigin
@RestController
@RequestMapping("/rest/roulette")
public class RouletteRestController {

    private final PreventAdminInterceptor preventAdminInterceptor;
	
	@Autowired
	private RouletteLogDao rouletteLogDao;
	@Autowired
	private RouletteDao rouletteDao;
	@Autowired
	private MemberDao memberDao;
	@Autowired
	private MemberPointLogDao memberPointLogDao;

    RouletteRestController(PreventAdminInterceptor preventAdminInterceptor) {
        this.preventAdminInterceptor = preventAdminInterceptor;
    }
	
    @RequestMapping("/insert")
	public String insert(@RequestParam String memberId,@RequestParam Integer point) {
    	
    	//세션과 회원이 맞지 않으면 진행 X
    	
    	
    	//오늘 이미 참여했다면 진행 X
    	//출석 체크 여부 조회
    			int rouletteCheck = rouletteLogDao.selectRouletteCkeck(memberId);
    			
    			if(rouletteCheck > 0) { //참여 상태라면
    				//이미 출석된 상태라고 안내 필요
    				return "already";
    			}
    			
    
		//로그 등록
		RouletteLogDto rouletteLogDto = new RouletteLogDto();
		rouletteLogDto.setRouletteLogNo(rouletteLogDao.sequence());
		rouletteLogDto.setRouletteLogRoulette(6);
		rouletteLogDto.setRouletteLogMember(memberId);
		rouletteLogDto.setRouletteLogPoint(point);
		
		rouletteLogDao.insert(rouletteLogDto);
		
		
		//회원 포인트 등록
		memberDao.addPoint(point, memberId);
		
		//포인트 로그 등록
		MemberPointLogDto memberPointLogDto = new MemberPointLogDto();
		memberPointLogDto.setPointLogNo(memberPointLogDao.sequence());
		memberPointLogDto.setPointLogMember(memberId);
		memberPointLogDto.setPointLogReason("룰렛이벤트");
		memberPointLogDto.setPointLogChange(point);	
		memberPointLogDao.insertByGiftpoint(memberPointLogDto);
		
		return "success";

	}
    
    //룰렛 돌리기 전 1일 참여여부 체크
    @RequestMapping("/check")
  	public String check(@RequestParam String memberId) {
      	
      	//세션과 회원이 맞지 않으면 진행 X
      	
      	
      	//오늘 이미 참여했다면 진행 X
      	//출석 체크 여부 조회
      			int rouletteCheck = rouletteLogDao.selectRouletteCkeck(memberId);
      			
      			if(rouletteCheck > 0) { //참여 상태라면
      				//이미 출석된 상태라고 안내 필요
      				return "already";
      			}
      			
      			return "success";
      			
  	}
	


}
