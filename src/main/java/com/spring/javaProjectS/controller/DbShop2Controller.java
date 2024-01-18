package com.spring.javaProjectS.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.spring.javaProjectS.pagination.PageProcess;
import com.spring.javaProjectS.pagination.PageVO;
import com.spring.javaProjectS.service.DbShopService;
import com.spring.javaProjectS.vo.DbBaesongVO;
import com.spring.javaProjectS.vo.DbCartVO;
import com.spring.javaProjectS.vo.DbOptionVO;
import com.spring.javaProjectS.vo.DbProductVO;

@Controller
@RequestMapping("/dbShop2")
public class DbShop2Controller {

	@Autowired
	DbShopService dbShopService;
	
	@Autowired
	PageProcess pageProcess;
	
	// 분류 등록폼 호출 및 출력
	//@RequestMapping(value = "/{adminFlag}", method = RequestMethod.GET)
	//public String dbCategoryGet(Model model, @PathVariable String adminFlag) {
	@RequestMapping(value = "/dbCategory", method = RequestMethod.GET)
	public String dbCategoryGet(Model model) {
		List<DbProductVO> mainVOS = dbShopService.getCategoryMain();			// 대분류 리스트
		List<DbProductVO> middleVOS = dbShopService.getCategoryMiddle();	// 중분류 리스트
		List<DbProductVO> subVOS = dbShopService.getCategorySub();				// 소분류 리스트
		
		model.addAttribute("mainVOS", mainVOS);
		model.addAttribute("middleVOS", middleVOS);
		model.addAttribute("subVOS", subVOS);
		model.addAttribute("adminFlag", "dbCategory");
		
		return "admin2/admin2";
	}
	
	// 대분류 등록하기
	@ResponseBody
	@RequestMapping(value = "/categoryMainInput", method=RequestMethod.POST)
	public String categoryMainInputPost(DbProductVO vo) {
		// 현재 기존에 생성된 대분류명이 있는지 체크...
		DbProductVO productVO = dbShopService.getCategoryMainOne(vo.getCategoryMainCode(), vo.getCategoryMainName());
		
		if(productVO != null) return "0";
		
		int res = dbShopService.setCategoryMainInput(vo);
		return res + "";
	}
	
	// 대분류 삭제하기
	@ResponseBody
	@RequestMapping(value = "/categoryMainDelete", method=RequestMethod.POST)
	public String categoryMainDeletePost(DbProductVO vo) {
		// 현재 기존에 생성된 대분류코드가 있는지 체크...
		DbProductVO middleVO = dbShopService.getCategoryMiddleOne(vo);		// 삭제할 대분류항목에 중분류데이터가 있는지 검색처리
		
		if(middleVO != null) return "0";
		
		int res = dbShopService.setCategoryMainDelete(vo.getCategoryMainCode());	//  대분류항목 삭제처리
		return res + "";
	}
	
	// 중분류 등록하기
	@ResponseBody
	@RequestMapping(value = "/categoryMiddleInput", method=RequestMethod.POST)
	public String categoryMiddleInputPost(DbProductVO vo) {
		// 기존에 생성된 같은 중분류명이 있는지 체크...
		DbProductVO productVO = dbShopService.getCategoryMiddleOne(vo);
		
		if(productVO != null) return "0";
		
		int res = dbShopService.setCategoryMiddleInput(vo);	// 중분류항목 저장하기
		return res + "";
	}
	
	// 중분류 삭제하기
	@ResponseBody
	@RequestMapping(value = "/categoryMiddleDelete", method=RequestMethod.POST)
	public String categoryMiddleDeletePost(DbProductVO vo) {
		// 중분류 하위항목이 있는지 체크...
		DbProductVO subVO = dbShopService.getCategorySubOne(vo);		// 삭제할 중분류항목에 소분류데이터가 있는지 검색처리
		
		if(subVO != null) return "0";
		
		int res = dbShopService.setCategoryMiddleDelete(vo.getCategoryMiddleCode());	//  중분류항목 삭제처리
		return res + "";
	}
	
	// 대분류명 선택하면 중분류명 가져오기
	@ResponseBody
	@RequestMapping(value = "/categoryMiddleName", method = RequestMethod.POST)
	public List<DbProductVO> categoryMiddleNamePost(String categoryMainCode) {
		return dbShopService.getCategoryMiddleName(categoryMainCode);
	}
	
	// 소분류 등록하기
	@ResponseBody
	@RequestMapping(value = "/categorySubInput", method=RequestMethod.POST)
	public String categorySubInputPost(DbProductVO vo) {
		// 기존에 생성된 같은 소분류명이 있는지 체크...
		DbProductVO productVO = dbShopService.getCategorySubOne(vo);
		
		if(productVO != null) return "0";
		
		int res = dbShopService.setCategorySubInput(vo);	// 소분류항목 저장하기
		return res + "";
	}
	
	// 중분류 선택시에 소분류항목명을 가져오기
	@ResponseBody
	@RequestMapping(value = "/categorySubName", method = RequestMethod.POST)
	public List<DbProductVO> categorySubNamePost(String categoryMainCode, String categoryMiddleCode) {
		return dbShopService.getCategorySubName(categoryMainCode, categoryMiddleCode);
	}
	
	// 소분류 삭제하기
	@ResponseBody
	@RequestMapping(value = "/categorySubDelete", method=RequestMethod.POST)
	public String categorySubDeletePost(DbProductVO vo) {
		// 소분류 하위항목(상품명)이 있는지 체크...
		DbProductVO categoryProdectVO = dbShopService.getCategoryProductName(vo);		// 삭제할 소분류항목에 상품이 있는지 검색처리
		
		if(categoryProdectVO != null) return "0";
		
		int res = dbShopService.setCategorySubDelete(vo.getCategorySubCode());	//  소분류항목 삭제처리
		return res + "";
	}
	
	// 관리자 상품등록시에 ckeditor에 그림을 올린다면 dbShop폴더에 저장되고, 저장된 파일을 브라우저 textarea상자에 보여준다.
	@ResponseBody
	@RequestMapping("/imageUpload")
	public void imageUploadGet(HttpServletRequest request, HttpServletResponse response, @RequestParam MultipartFile upload) throws Exception {
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		
		String originalFilename = upload.getOriginalFilename();
		
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
		originalFilename = sdf.format(date) + "_" + originalFilename;
		
		byte[] bytes = upload.getBytes();
		
		String uploadPath = request.getSession().getServletContext().getRealPath("/resources/data/dbShop/");
		OutputStream outStr = new FileOutputStream(new File(uploadPath + originalFilename));
		outStr.write(bytes);
		
		PrintWriter out = response.getWriter();
		String fileUrl = request.getContextPath() + "/data/dbShop/" + originalFilename;
		out.println("{\"originalFilename\":\""+originalFilename+"\",\"uploaded\":1,\"url\":\""+fileUrl+"\"}");
		
		out.flush();
		outStr.close();
	}
	
  // 상품 등록을 위한 폼 보기..
	@RequestMapping(value = "/dbProduct", method=RequestMethod.GET)
	public String dbProductGet(Model model) {
		List<DbProductVO> mainVos = dbShopService.getCategoryMain();
		model.addAttribute("mainVos", mainVos);
		model.addAttribute("adminFlag", "dbProduct");
		return "admin2/admin2";
	}
	
	// 상품 등록 처리하기
	@RequestMapping(value = "/dbProduct", method=RequestMethod.POST)
	public String dbProductPost(MultipartFile file, DbProductVO vo) {
		// 이미지파일 업로드시에 dbShop폴더에서 'dbShop/product'폴더로 복사처리...후~ 처리된 내용을 DB에 저장하기
		int res = dbShopService.imgCheckProductInput(file, vo);
		
		if(res != 0) return "redirect:/message/dbProductInputOk";
		return "redirect:/message/dbProductInputNo";
	}
	
  // 등록된 모든 상품 리스트 보기(관리자화면에서...)
	@SuppressWarnings("unused")
	@RequestMapping(value = "/dbShopList", method = RequestMethod.GET)
	public String dbShopListGet(Model model,
			@RequestParam(name="part", defaultValue = "전체", required = false) String part) {
		// 소분류명을 가져온다.
		List<DbProductVO> subTitleVOS = dbShopService.getSubTitle();
		model.addAttribute("subTitleVOS", subTitleVOS);
		model.addAttribute("part", part);

		// 전체 상품리스트 가져오기
		List<DbProductVO> productVOS = dbShopService.getDbShopList(part);
		model.addAttribute("productVOS", productVOS);
		
		model.addAttribute("adminFlag", "dbShopList");
		return "admin2/admin2";
	}
	
	// 관리자에서 진열된 상품을 클릭하였을경우에 해당 상품의 상세내역 보여주기
	@RequestMapping(value = "/dbShopContent", method = RequestMethod.GET)
	public String dbShopContentGet(Model model, int idx) {
		DbProductVO productVO = dbShopService.getDbShopProduct(idx);			// 상품 1건의 정보를 불러온다.
		List<DbOptionVO> optionVOS = dbShopService.getDbShopOption(idx);	// 해당 상품의 모든 옵션 정보를 가져온다.
		
		model.addAttribute("productVO", productVO);
		model.addAttribute("optionVOS", optionVOS);
		 
		model.addAttribute("adminFlag", "dbShopContent");
		return "admin2/admin2";
	}
	
	// 옵션 등록창 보여주기(관리자 왼쪽메뉴에서 선택시 처리)
	@RequestMapping(value = "/dbOption", method = RequestMethod.GET)
	public String dbOptionGet(Model model) {
		List<DbProductVO> mainVos = dbShopService.getCategoryMain();
		model.addAttribute("mainVos", mainVos);
				
		model.addAttribute("adminFlag", "dbOption");
		return "admin2/admin2";
	}
	
	// 소분류 선택시에 해당 상품명(모델명)을 가져오기
	@ResponseBody
	@RequestMapping(value = "/categoryProductName", method = RequestMethod.POST)
	public List<DbProductVO> categoryProductNameGet(String categoryMainCode, String categoryMiddleCode, String categorySubCode) {
		return dbShopService.getCategoryProductNameAjax(categoryMainCode, categoryMiddleCode, categorySubCode);
	}
	
	// 옵셥보기에서 상품선택 콤보상자에서 상품을 선택시 해당 상품의 정보를 보여준다.
	@ResponseBody
	@RequestMapping(value = "/getProductInfor", method = RequestMethod.POST)
	public DbProductVO getProductInforGet(String productName) {
		return dbShopService.getProductInfor(productName);
	}
	
	// 옵셥보기에서 '옵션보기'버튼 클릭시 해당 상품의 옵션리스트를 보여준다.
	@ResponseBody
	@RequestMapping(value = "/getOptionList", method = RequestMethod.POST)
	public List<DbOptionVO> getOptionListPost(int productIdx) {
		return dbShopService.getOptionList(productIdx);
	}
	
	// 옵션에 기록한 내용들을 등록처리하기
	@RequestMapping(value = "/dbOption", method = RequestMethod.POST)
	public String dbOptionPost(Model model, DbOptionVO vo, String[] optionName, int[] optionPrice) {
		int res = 0;
		for(int i=0; i<optionName.length; i++) {
			int optionCnt = dbShopService.getOptionSame(vo.getProductIdx(), optionName[i]);
			if(optionCnt != 0) continue;
			
			// 동일한 옵션이 없다면 vo에 현재 옵션 이름과 가격을 set시킨후 옵션테이블에 등록처리한다.
			vo.setProductIdx(vo.getProductIdx());
			vo.setOptionName(optionName[i]);
			vo.setOptionPrice(optionPrice[i]);
			
			res = dbShopService.setDbOptionInput(vo);
		}
		if(res != 0) return "redirect:/message/dbOptionInput2Ok";
		else return "redirect:/message/dbOptionInputNo";
	}
	
	// 옵션 등록창에서 옵션리스트를 확인후 필요없는 옵션항목을 삭제처리..
	@ResponseBody
	@RequestMapping(value="/optionDelete", method = RequestMethod.POST)
	public String optionDeletePost(int idx) {
		int res = dbShopService.setOptionDelete(idx);
		return res + "";
	}
	
  // 관리자에서 관리자가 주문 확인하기
	@RequestMapping(value="/adminOrderStatus")
	public String dbOrderProcessGet(Model model,
    @RequestParam(name="startJumun", defaultValue="", required=false) String startJumun,
    @RequestParam(name="endJumun", defaultValue="", required=false) String endJumun,
    @RequestParam(name="orderStatus", defaultValue="전체", required=false) String orderStatus,
    @RequestParam(name="pag", defaultValue="1", required=false) int pag,
    @RequestParam(name="pageSize", defaultValue="5", required=false) int pageSize) {

		List<DbBaesongVO> vos = null;
		PageVO pageVO = null;
		String strNow = "";
		if(startJumun.equals("")) {
			Date now = new Date();
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    strNow = sdf.format(now);
	    
	    startJumun = strNow;
	    endJumun = strNow;
		}
    
    String strOrderStatus = startJumun + "@" + endJumun + "@" + orderStatus;
    pageVO = pageProcess.totRecCnt(pag, pageSize, "adminDbOrderProcess", "", strOrderStatus);

		vos = dbShopService.getAdminOrderStatus(pageVO.getStartIndexNo(), pageSize, startJumun, endJumun, orderStatus);
	
	  model.addAttribute("startJumun", startJumun);
	  model.addAttribute("endJumun", endJumun);
	  model.addAttribute("orderStatus", orderStatus);
	  model.addAttribute("vos", vos);
	  model.addAttribute("pageVO", pageVO);
	
	  //return "admin2/dbShop/dbOrderProcess";
	  model.addAttribute("adminFlag", "dbOrderProcess");
		return "admin2/admin2";
	}
	
	// 주문관리에서, 관리자가 주문상태를 변경처리하는것
	@ResponseBody
	@RequestMapping(value="/goodsStatus", method=RequestMethod.POST)
	public String goodsStatusGet(String orderIdx, String orderStatus) {
		int res = dbShopService.setOrderStatusUpdate(orderIdx, orderStatus);
		return res + "";
	}
	
	
		
/*  =========== 위쪽은 관리자 처리 화면입니다.......  ====================================================================================== */
	
	
	
/*  =========== 아래쪽은 사용자(고객) 처리 화면입니다.......  ====================================================================================== */
	
	// 등록된 상품 진열하기(보여주기) - 고객화면에 출력
	@RequestMapping(value = "/dbProductList", method = RequestMethod.GET)
	public String dbProductListGet(Model model,
			@RequestParam(name="part", defaultValue = "전체", required = false) String part) {
		// 소분류명을 가져온다.
		List<DbProductVO> subTitleVOS = dbShopService.getSubTitle();
		model.addAttribute("subTitleVOS", subTitleVOS);
		model.addAttribute("part", part);

		// 전체 상품리스트 가져오기
		List<DbProductVO> productVOS = dbShopService.getDbShopList(part);
		model.addAttribute("productVOS", productVOS);
		
		return "dbShop/dbProductList";
	}
	
  // 진열된 상품클릭시 해당상품의 상세정보 보여주기(사용자(고객)화면에서 보여주기)
	@RequestMapping(value="/dbProductContent", method=RequestMethod.GET)
	public String dbProductContentGet(int idx, Model model) {
		DbProductVO productVo = dbShopService.getDbShopProduct(idx);			// 상품의 상세정보 불러오기
		List<DbOptionVO> optionVos = dbShopService.getDbShopOption(idx);	// 옵션의 모든 정보 불러오기
		
		model.addAttribute("productVo", productVo);
		model.addAttribute("optionVos", optionVos);
		
		return "dbShop/dbProductContent";
	}

	
	@RequestMapping(value="/dbProductContent", method=RequestMethod.POST)
	public String dbProductContentPost(DbCartVO vo, HttpSession session, String flag) {
		String mid = (String) session.getAttribute("sMid");
		DbCartVO resVo = dbShopService.getDbCartProductOptionSearch(vo.getProductName(), vo.getOptionName(), mid);
		int res = 0;
		if(resVo != null) {
			String[] voOptionNums = vo.getOptionNum().split(",");
			String[] resOptionNums = resVo.getOptionNum().split(",");
			int[] nums = new int[99];
			String strNums = "";
			for(int i=0; i<voOptionNums.length; i++) {
				nums[i] += (Integer.parseInt(voOptionNums[i]) + Integer.parseInt(resOptionNums[i]));
				strNums += nums[i];
				if(i < nums.length - 1) strNums += ",";
			}
			vo.setOptionNum(strNums);
			res = dbShopService.dbShopCartUpdate(vo);
		}
		else {
			res = dbShopService.dbShopCartInput(vo);
		}
		
		if(res != 0) {
			if(flag.equals("order")) {
				return "redirect:/message/cartOrderOk";
			}
			else {
				return "redirect:/message/cartInputOk";
			}
		}
		else return "redirect:/message/cartOrderNo";
	}
	
	
	@RequestMapping(value="/dbCartList", method=RequestMethod.GET)
	public String dbCartGet(HttpSession session, DbCartVO vo, Model model) {
		String mid = (String) session.getAttribute("sMid");
		List<DbCartVO> vos = dbShopService.getDbCartList(mid);
		
		if(vos.size() == 0) {
			return "redirect:/message/cartEmpty";
		}
		
		model.addAttribute("cartListVOS", vos);
		return "dbShop/dbCartList";
	}
	
	
	@ResponseBody
	@RequestMapping(value="/dbCartDelete", method=RequestMethod.POST)
	public String dbCartDeleteGet(int idx) {
		int res = dbShopService.dbCartDelete(idx);
		return res + "";
	}
	
}
