package com.spring.javaProjectS.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.javaProjectS.pagination.PageProcess;
import com.spring.javaProjectS.pagination.PageVO;
import com.spring.javaProjectS.service.AdminService;
import com.spring.javaProjectS.service.MemberService;
import com.spring.javaProjectS.vo.DbPayMentVO;
import com.spring.javaProjectS.vo.MemberVO;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	AdminService adminService;
	
	@Autowired
	MemberService memberService;
	
	@Autowired
	PageProcess pageProcess;
	
	@RequestMapping(value = "/adminMain", method = RequestMethod.GET)
	public String adminMain() {
		return "admin/adminMain";
	}
	
	@RequestMapping(value = "/adminLeft", method = RequestMethod.GET)
	public String adminLeft() {
		return "admin/adminLeft";
	}
	
	@RequestMapping(value = "/adminContent", method = RequestMethod.GET)
	public String adminContent() {
		return "admin/adminContent";
	}
	
	@RequestMapping(value = "/admin2", method = RequestMethod.GET)
	public String admin2(Model model) {
		model.addAttribute("adminFlag", "admin2");
		return "admin2/admin2";
	}
	
	@RequestMapping(value = "/login2", method = RequestMethod.GET)
	public String login2(Model model) {
		model.addAttribute("adminFlag", "login");
		return "admin2/admin2";
	}
	
	@RequestMapping(value = "/register2", method = RequestMethod.GET)
	public String register2(Model model) {
		model.addAttribute("adminFlag", "register");
		return "admin2/admin2";
	}
	
	@RequestMapping(value = "/password2", method = RequestMethod.GET)
	public String password2(Model model) {
		model.addAttribute("adminFlag", "password");
		return "admin2/admin2";
	}
	
	@RequestMapping(value = "/charts", method = RequestMethod.GET)
	public String charts(Model model) {
		model.addAttribute("adminFlag", "charts");
		return "admin2/admin2";
	}
	
	@RequestMapping(value = "/tables", method = RequestMethod.GET)
	public String tables(Model model) {
		model.addAttribute("adminFlag", "tables");
		return "admin2/admin2";
	}
	
	@RequestMapping(value = "/401", method = RequestMethod.GET)
	public String a401Get(Model model) {
		model.addAttribute("adminFlag", "401");
		return "admin2/admin2";
	}
	
	@RequestMapping(value = "/404", method = RequestMethod.GET)
	public String a404Get(Model model) {
		model.addAttribute("adminFlag", "404");
		return "admin2/admin2";
	}
	
	@RequestMapping(value = "/500", method = RequestMethod.GET)
	public String a500Get(Model model) {
		model.addAttribute("adminFlag", "500");
		return "admin2/admin2";
	}
	
	@RequestMapping(value = "/payment", method = RequestMethod.GET)
	public String paymentGet(Model model) {
		model.addAttribute("adminFlag", "payment");
		return "admin2/admin2";
	}
	
	@RequestMapping(value = "/payment", method = RequestMethod.POST)
	public String paymentPost(Model model, HttpSession session, DbPayMentVO vo) {
		session.setAttribute("sPayMentVO", vo);
		model.addAttribute("vo", vo);
		model.addAttribute("adminFlag", "sample");
		return "admin2/admin2";
	}
	
	@RequestMapping(value = "/paymentOk", method = RequestMethod.GET)
	public String paymentOkGet(Model model, HttpSession session) {
		DbPayMentVO vo = (DbPayMentVO) session.getAttribute("sPayMentVO");
		model.addAttribute("vo", vo);
		session.removeAttribute("sPayMentVO");
		model.addAttribute("adminFlag", "paymentOk");
		return "admin2/admin2";
	}
	
	// 회원 전체 검색
	@RequestMapping(value = "/member/adminMemberList", method = RequestMethod.GET)
	public String adminMemberListGet(Model model,
			@RequestParam(name="mid", defaultValue = "", required = false) String mid,
			@RequestParam(name="pag", defaultValue = "1", required = false) int pag,
			@RequestParam(name="pageSize", defaultValue = "10", required = false) int pageSize) {
		
		PageVO pageVo = pageProcess.totRecCnt(pag, pageSize, "member", "", mid);
		
		List<MemberVO> vos = memberService.getMemberList(pageVo.getStartIndexNo(), pageSize, mid);
		
		model.addAttribute("vos", vos);
		model.addAttribute("pageVo", pageVo);
		
		model.addAttribute("mid", mid);
		
		return "admin/member/adminMemberList";
	}
	
	// 회원 개별 검색
	@RequestMapping(value = "/member/adminMemberSearch", method = RequestMethod.POST)
	public String adminMemberSearchPost(Model model,
			@RequestParam(name="mid", defaultValue = "", required = false) String mid,
			@RequestParam(name="pag", defaultValue = "1", required = false) int pag,
			@RequestParam(name="pageSize", defaultValue = "10", required = false) int pageSize) {
		
		PageVO pageVo = pageProcess.totRecCnt(pag, pageSize, "member", "", mid);
		
		List<MemberVO> vos = memberService.getMemberList(pageVo.getStartIndexNo(), pageSize, mid);
		
		model.addAttribute("vos", vos);
		model.addAttribute("pageVo", pageVo);
		
		model.addAttribute("mid", mid);
		
		return "admin/member/adminMemberList";
	}
	
	// 회원 등급 변경하기
	@ResponseBody
	@RequestMapping(value = "/member/adminMemberLevel", method = RequestMethod.POST)
	public String adminMemberLevelPost(int idx, int level) {
		int res = adminService.setMemberLevelCheck(idx, level);
		return res+"";
	}
}
