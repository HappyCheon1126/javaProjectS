package com.spring.javaProjectS.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.RandomStringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.spring.javaProjectS.common.ARIAUtil;
import com.spring.javaProjectS.common.SecurityUtil;
import com.spring.javaProjectS.service.StudyService;
import com.spring.javaProjectS.vo.ChartVO;
import com.spring.javaProjectS.vo.KakaoAddressVO;
import com.spring.javaProjectS.vo.MailVO;
import com.spring.javaProjectS.vo.QrCodeVO;
import com.spring.javaProjectS.vo.TransactionVO;
import com.spring.javaProjectS.vo.UserVO;

@Controller
@RequestMapping("/study")
public class StudyController {
  
	@Autowired
	StudyService studyService;
	
	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	JavaMailSender mailSender;
	
	@RequestMapping(value = "/ajax/ajaxForm", method = RequestMethod.GET)
	public String ajaxFormGet() {
		
		return "study/ajax/ajaxForm";
	}
	
	@ResponseBody
	@RequestMapping(value = "/ajax/ajaxTest1", method = RequestMethod.POST)
	public String ajaxTest1Get(int idx) {
		System.out.println("idx : " + idx);
		
		return idx+"";
	}
	
	@ResponseBody
	@RequestMapping(value = "/ajax/ajaxTest2", method = RequestMethod.POST, produces="application/text; charset=utf8")
	public String ajaxTest2Get(String str) {
		System.out.println("str : " + str);
		
		return str;
	}
	
	@RequestMapping(value = "/ajax/ajaxTest3_1", method = RequestMethod.GET)
	public String ajaxTest3_1Get(String str) {
		return "study/ajax/ajaxTest3_1";
	}
	
	@ResponseBody
	@RequestMapping(value = "/ajax/ajaxTest3_1", method = RequestMethod.POST)
	public String[] ajaxTest3_1Post(String dodo) {
		//String[] strArray = new String[100];
		//strArray = studyService.getCityStringArray(dodo);
		//return strArray;
		return studyService.getCityStringArray(dodo);
	}
	
	@RequestMapping(value = "/ajax/ajaxTest3_2", method = RequestMethod.GET)
	public String ajaxTest3_2Get() {
		return "study/ajax/ajaxTest3_2";
	}
	
	@ResponseBody
	@RequestMapping(value = "/ajax/ajaxTest3_2", method = RequestMethod.POST)
	public ArrayList<String> ajaxTest3_2Post(String dodo) {
		return studyService.getCityArrayList(dodo);
	}
	
	@RequestMapping(value = "/ajax/ajaxTest3_3", method = RequestMethod.GET)
	public String ajaxTest3_3Get() {
		return "study/ajax/ajaxTest3_3";
	}
	
	@ResponseBody
	@RequestMapping(value = "/ajax/ajaxTest3_3", method = RequestMethod.POST)
	public HashMap<Object, Object> ajaxTest3_3Post(String dodo) {
		ArrayList<String> vos = studyService.getCityArrayList(dodo);
		
		HashMap<Object, Object> map = new HashMap<Object, Object>();
		map.put("city", vos);
		
		return map;
	}
	
	@RequestMapping(value = "/ajax/ajaxTest4_1", method = RequestMethod.GET)
	public String ajaxTest4_1Get() {
		return "study/ajax/ajaxTest4_1";
	}
	
	@ResponseBody
	@RequestMapping(value = "/ajax/ajaxTest4_1", method = RequestMethod.POST)
	public UserVO ajaxTest4_1Post(String mid) {
		return studyService.getUserSearch(mid);
	}
	
	@ResponseBody
	@RequestMapping(value = "/ajax/ajaxTest4_2", method = RequestMethod.POST)
	public List<UserVO> ajaxTest4_2Post(String mid) {
		return studyService.getUser2SearchMid(mid);
	}

	@RequestMapping(value = "/uuid/uidForm", method = RequestMethod.GET)
	public String uidFormGet() {
		return "study/uuid/uidForm";
	}
	
	@ResponseBody
	@RequestMapping(value = "/uuid/uidForm", method = RequestMethod.POST, produces="application/text; charset=utf8")
	public String uidFormPost() {
		UUID uid = UUID.randomUUID();
		return uid.toString();
	}
	
	@RequestMapping(value = "/password/sha256", method = RequestMethod.GET)
	public String sha256Get() {
		return "study/password/sha256";
	}
	
	@ResponseBody
	@RequestMapping(value = "/password/sha256", method = RequestMethod.POST, produces="application/text; charset=utf8")
	public String sha256Post(String pwd) {
		UUID uid = UUID.randomUUID();
		String salt = uid.toString().substring(0,8);
		
		SecurityUtil security = new SecurityUtil();
		String encPwd = security.encryptSHA256(pwd+salt);
		
		pwd = "원본 비밀번호 : " + pwd + " / salt키 : " + salt + " / 암호화된 비밀번호 : " + encPwd;
		
		return pwd;
	}
	
	@RequestMapping(value = "/password/aria", method = RequestMethod.GET)
	public String ariaGet() {
		return "study/password/aria";
	}
	
	@ResponseBody
	@RequestMapping(value = "/password/aria", method = RequestMethod.POST, produces="application/text; charset=utf8")
	public String ariaPost(String pwd) throws InvalidKeyException, UnsupportedEncodingException {
		UUID uid = UUID.randomUUID();
		String salt = uid.toString().substring(0,8);
		
		String encPwd = "";
		String decPwd = "";
		
		encPwd = ARIAUtil.ariaEncrypt(pwd + salt);
		decPwd = ARIAUtil.ariaDecrypt(encPwd);
		
		pwd = "원본 비밀번호 : " + pwd + "/ salt : " + salt + " / 암호화된 비밀번호 : " + encPwd + " / 복호화된 비밀번호 : " + decPwd;
		
		return pwd;
	}
	
	@RequestMapping(value = "/password/bCryptPassword", method = RequestMethod.GET)
	public String bCryptPasswordGet() {
		return "study/password/bCryptPassword";
	}
	
	// BcryptPasswordEncoder 암호화
	@ResponseBody
	@RequestMapping(value = "/password/bCryptPassword", method = RequestMethod.POST, produces="application/text; charset=utf8")
	public String bCryptPasswordPost(String pwd) {
		String encPwd = "";
		encPwd = passwordEncoder.encode(pwd);
		
		pwd = "원본 비밀번호 : " + pwd + "/ 암호화된 비밀번호 : " + encPwd;
		
		return pwd;
	}
	
	// 메일 전송폼 호출
	@RequestMapping(value = "/mail/mail", method = RequestMethod.GET)
	public String mailGet() {
		return "study/mail/mailForm";
	}
	
	// 메일 전송하기
	@RequestMapping(value = "/mail/mail", method = RequestMethod.POST)
	public String mailPost(MailVO vo, HttpServletRequest request) throws MessagingException {
		String toMail = vo.getToMail();
		String title = vo.getTitle();
		String content = vo.getContent();
		
		// 메일 전송을 위한 객체 : MimeMessage(), MimeMessageHelper()
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
		
		// 메일보관함에 회원이 보내온 메세지들의 정보를 모두 저장시킨후 작업처리하자...
		messageHelper.setTo(toMail);
		messageHelper.setSubject(title);
		messageHelper.setText(content);
		
		// 메세지 보관함의 내용(content)에, 발신자의 필요한 정보를 추가로 담아서 전송시켜주면 좋다....
		content = content.replace("\n", "<br>");
		content += "<br><hr><h3>JavaProjectS 보냅니다.</h3><hr><br>";
		content += "<p><img src=\"cid:main.jpg\" width='500px'></p>";
		content += "<p>방문하기 : <a href='49.142.157.251:9090/cjgreen'>JavaProject</a></p>";
		content += "<hr>";
		messageHelper.setText(content, true);
		
		// 본문에 기재된 그림파일의 경로와 파일명을 별로도 표시한다. 그런후 다시 보관함에 저장한다.
		//FileSystemResource file = new FileSystemResource("D:\\JavaProject\\springframework\\works\\javaProjectS\\src\\main\\webapp\\resources\\images\\main.jpg");
		//request.getSession().getServletContext().getRealPath("");
		FileSystemResource file = new FileSystemResource(request.getSession().getServletContext().getRealPath("/resources/images/main.jpg"));
		messageHelper.addInline("main.jpg", file);
		
		// 첨부파일 보내기
		file = new FileSystemResource(request.getSession().getServletContext().getRealPath("/resources/images/chicago.jpg"));
		messageHelper.addAttachment("chicago.jpg", file);
		
		file = new FileSystemResource(request.getSession().getServletContext().getRealPath("/resources/images/main.zip"));
		messageHelper.addAttachment("main.zip", file);
		
		
		// 메일 전송하기
		mailSender.send(message);
		
		return "redirect:/message/mailSendOk";
	}
	
	@RequestMapping(value = "/fileUpload/fileUpload", method = RequestMethod.GET)
	public String fileUploadGet(HttpServletRequest request, Model model) {
		String realPath = request.getSession().getServletContext().getRealPath("/resources/data/study");
		
		String[] files = new File(realPath).list();
		
		model.addAttribute("files", files);
		model.addAttribute("fileCount", files.length);
		
		return "study/fileUpload/fileUpload";
	}
	
	@RequestMapping(value = "/fileUpload/fileUpload", method = RequestMethod.POST)
	public String fileUploadPost(MultipartFile fName, String mid) {
		
		int res = studyService.fileUpload(fName, mid);
		
		//return "study/fileUpload/fileUpload";
		if(res == 1) return "redirect:/message/fileUploadOk";
		else return "redirect:/message/fileUploadNo";
	}
	
	// study폴더의 파일 삭제하기
	@ResponseBody
	@RequestMapping(value = "/fileUpload/fileDelete", method = RequestMethod.POST)
	public String fileDeletePost(HttpServletRequest request,
			@RequestParam(name="file", defaultValue = "", required=false) String fName) {
		String realPath = request.getSession().getServletContext().getRealPath("/resources/data/study/");
		
		int res = 0;
		File file = new File(realPath + fName);
		
		if(file.exists()) {
			file.delete();
			res = 1;
		}
		
		return res + "";
	}
	
	@RequestMapping(value = "/fileUpload/fileDownAction", method = RequestMethod.GET)
	public void fileDownAction(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String file = request.getParameter("file");
		String realPath = request.getSession().getServletContext().getRealPath("/resources/data/study/");
		
		File downFile = new File(realPath + file);
		
		String downFileName = new String(file.getBytes("UTF-8"), "8859_1");
		response.setHeader("Content-Disposition", "attachment:filename=" + downFileName);
		
		FileInputStream fis = new FileInputStream(downFile);
		ServletOutputStream sos = response.getOutputStream();
		
		byte[] bytes = new byte[2048];
		int data = 0;
		while((data = fis.read(bytes, 0, bytes.length)) != -1) {
			sos.write(bytes, 0, data);
		}
		sos.flush();
		sos.close();
		fis.close();
	}
	
	@RequestMapping(value = "/fileUpload/multiFile", method = RequestMethod.GET)
	public String multiFileGet() {
		return "study/fileUpload/multiFile";
	}
	
	//@ResponseBody
	@RequestMapping(value = "/fileUpload/multiFile", method = RequestMethod.POST)
	public String multiFilePost(MultipartHttpServletRequest mFile, HttpServletRequest request) {
		
		String[] imgNames = request.getParameter("imgNames").split("/");
		
		int res = studyService.multiFileUpload(mFile, imgNames);
		
		if(res == 1) return "redirect:/message/multiFileUploadOk";
		else return "redirect:/message/multiFileUploadNo";
		
		
		//int res = studyService.multiFileUpload(data);
		//return "";
	}
	
	@RequestMapping(value = "/fileUpload/fileDeleteAll", method = RequestMethod.GET)
	public String fileDeleteAllGet() {
		return "redirect:/message/fileDeleteAllOk";
	}
	
	// study폴더의 모든 파일 삭제처리하기
	@ResponseBody
	@RequestMapping(value = "/fileUpload/fileDeleteAll", method = RequestMethod.POST)
	public String fileDeleteAllPost(HttpServletRequest request,
			@RequestParam(name="file", defaultValue = "", required=false) String fName) {
		int res = 0;
		String realPath = request.getSession().getServletContext().getRealPath("/resources/data/study/");
		File targetFolder = new File(realPath);
		
		if(!targetFolder.exists()) {
      System.out.println(targetFolder + " 경로가 존재하지 않습니다.");
      return res + "";
		}
		
		File[] files = targetFolder.listFiles();
		if(files.length != 0) {
	    for(File file : files) {
	        if(!file.isDirectory()) {
	        	file.delete();
	        }
	    }
			res = 1;
		}
		//File file = new File(realPath + fName);
		//if(file.exists()) {
		//	file.delete();
		//	res = 1;
		//}
		
		return res + "";
	}
	
	// 카카오맵 연습 기본
	@RequestMapping(value = "/kakao/kakaomap", method = RequestMethod.GET)
	public String kakaomapGet() {
		return "study/kakao/kakaomap";
	}
	
	// 카카오맵 연습1
	@RequestMapping(value = "/kakao/kakaoEx1", method = RequestMethod.GET)
	public String kakaoEx1Get() {
		return "study/kakao/kakaoEx1";
	}
	
	// 카카오맵 연습1(선택한 지점명을 DB에 저장하기)
	@ResponseBody
	@RequestMapping(value = "/kakao/kakaoEx1", method = RequestMethod.POST)
	public String kakaoEx1Post(KakaoAddressVO vo) {
		KakaoAddressVO searchVO = studyService.getKakaoAddressSearch(vo.getAddress());
		
		if(searchVO != null) return "0";
		
		studyService.setKakaoAddressInput(vo);
		
		return "1";
	}
	
	// 카카오맵 연습2(MyDB에 저장된 주소목록 가져오기 / 지점검색하기 추가)
	@RequestMapping(value = "/kakao/kakaoEx2", method = RequestMethod.GET)
	public String kakaoEx2Get(Model model,
			@RequestParam(name="address", defaultValue = "", required = false) String address) {
		KakaoAddressVO vo = new KakaoAddressVO();
		List<KakaoAddressVO> vos = studyService.getKakaoAddressList();
		
		if(address.equals("")) {
			vo.setLatitude(36.63510627148798);
			vo.setLongitude(127.4595239897276);
		}
		else {
		  vo = studyService.getKakaoAddressSearch(address);
		}
		
		model.addAttribute("vos", vos);
		model.addAttribute("vo", vo);
		
		return "study/kakao/kakaoEx2";
	}
	
	// 카카오맵 연습2(MyDB에 저장된 주소 삭제하기)
	@ResponseBody
	@RequestMapping(value = "/kakao/kakaoAddressDelete", method = RequestMethod.POST)
	public String kakaoEx2Post(
			@RequestParam(name="address", defaultValue = "", required = false) String address) {
		int res = 0;
		System.out.println("address" + address);
		res = studyService.setKakaoAddressDelete(address);
		
		return res + "";
	}
	
	// 카카오맵 연습3(KakaoDB에 저장된 지명으로 검색후 MyDB에 주소 저장하기)
	@RequestMapping(value = "/kakao/kakaoEx3", method = RequestMethod.GET)
	public String kakaoEx3Get(Model model,
			@RequestParam(name="address", defaultValue = "청주시청", required = false) String address) {
		model.addAttribute("address", address);
		return "study/kakao/kakaoEx3";
	}
	
	// 차트연습(다양한 차트 그려보기)
	@RequestMapping(value = "/chart/chart", method = RequestMethod.GET)
	public String chartGet(Model model,
			@RequestParam(name="part", defaultValue = "", required = false) String part) {
		model.addAttribute("part", part);
		return "study/chart/chart";
	}
	
	// 차트연습(다양한 차트) 보여주기
	@RequestMapping(value = "/chart2/chart", method = RequestMethod.GET)
	public String chart2Get(Model model, ChartVO vo) {
		model.addAttribute("vo", vo);
		
		return "study/chart2/chart";
	}
	
	// 차트연습(다양한 차트 분석처리)
	@RequestMapping(value = "/chart2/chart", method = RequestMethod.POST)
	public String chart2Post(Model model, ChartVO vo) {
		model.addAttribute("vo", vo);
		return "study/chart2/chart";
	}
	// 차트연습(최근방문횟수 분석처리)
	@RequestMapping(value = "/chart2/visitCount", method = RequestMethod.GET)
	public String chart2Get(Model model,
			@RequestParam(name="part", defaultValue="", required=false) String part) {
		//System.out.println("part  " + part);
		List<ChartVO> vos = studyService.getVisitCount();	// 최근 8일간 방문한 총 횟수를 가져온다.
		String[] visitDates = new String[8];
		int[] visitDays = new int[8];
		int[] visitCounts = new int[8];
		for(int i=0; i<visitDates.length; i++) {
			visitDates[i] = vos.get(i).getVisitDate().replaceAll("-", "").substring(4);
			visitDays[i] = Integer.parseInt(vos.get(i).getVisitDate().toString().substring(8));
			visitCounts[i] = vos.get(i).getVisitCount();
		}
		
		model.addAttribute("title", "최근 8일간 방문횟수");
		model.addAttribute("subTitle", "최근 8일동안 방문한 해당일자 방문자 총수를 표시합니다.");
		model.addAttribute("visitCount", "방문횟수");
		model.addAttribute("legend", "일일 방문 총횟수");
		model.addAttribute("topTitle", "방문날짜");
		model.addAttribute("xTitle", "방문날짜");
		model.addAttribute("part", part);
		model.addAttribute("visitDates", visitDates);
		model.addAttribute("visitDays", visitDays);
		model.addAttribute("visitCounts", visitCounts);
		return "study/chart2/chart";
	}
	
	// randomAlphaNumeric : 알파벳과 숫자를 랜덤하게 출력하기 폼.
	@RequestMapping(value = "/captcha/randomAlphaNumeric", method = RequestMethod.GET)
	public String randomAlphaNumericGet() {
		return "study/captcha/randomAlphaNumeric";
	}
	
	// randomAlphaNumeric : 알파벳과 숫자를 랜덤하게 출력하기 처리...
	@ResponseBody
	@RequestMapping(value = "/captcha/randomAlphaNumeric", method = RequestMethod.POST)
	public String randomAlphaNumericPost() {
		String res = RandomStringUtils.randomAlphanumeric(64);
		return res;
	}
	
	// 캡차 : 사람과 기계 구별하기 폼...
	@RequestMapping(value = "/captcha/captcha", method = RequestMethod.GET)
	public String chptchaGet() {
		//return "study/captcha/captcha";
		return "redirect:/study/captcha/captchaImage";
	}
	
	// 캡차 이미지 만들기...
	//@ResponseBody
	//@RequestMapping(value = "/captcha/captchaImage", method = RequestMethod.POST)
	@RequestMapping(value = "/captcha/captchaImage", method = RequestMethod.GET)
	public String chptchaImagePost(HttpSession session, HttpServletRequest request, Model model) {
		// 시스템에 설정된 폰트 출력해보기
//		Font[] fontList = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
//		for(Font f : fontList) {
//			System.out.println(f.getName());
//		}
		
		try {
			// 알파뉴메릭문자 5개를 가져온다.
			String randomString = RandomStringUtils.randomAlphanumeric(5);
			System.out.println("randomString : " + randomString);
			session.setAttribute("sCaptcha", randomString);
			
			Font font = new Font("Jokerman", Font.ITALIC, 30);
			FontRenderContext frc = new FontRenderContext(null, true, true);
			Rectangle2D bounds = font.getStringBounds(randomString, frc);
			int w = (int) bounds.getWidth();
			int h = (int) bounds.getHeight();
			
			// 이미지로 생성하기
			BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = image.createGraphics();
			
			g.fillRect(0, 0, w, h);
			g.setColor(new Color(0, 156, 240));
			g.setFont(font);
			// 각종 랜더링 명령어에 의한 chptcha 문자 작업..(생략)..
			g.drawString(randomString, (float)bounds.getX(), (float)-bounds.getY());
			g.dispose();
			
			String realPath = request.getSession().getServletContext().getRealPath("/resources/data/study/");
			int temp = (int)(Math.random()*5) + 1;
			String captchaImage = "captcha" + temp + ".png";
			//ImageIO.write(image, "png", new File(realPath + "captcha.png"));
			ImageIO.write(image, "png", new File(realPath + captchaImage));
			model.addAttribute("captchaImage", captchaImage);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "study/captcha/captcha";
	}
	
	// 캡차확인하기 처리
	@ResponseBody
	@RequestMapping(value = "/captcha/captcha", method = RequestMethod.POST)
	public String chptchaPost(HttpSession session, String strCaptcha) {
		if(strCaptcha.equals(session.getAttribute("sCaptcha").toString())) return "1";
		else return "0";
	}
	
	// QR Code 폼 보기
	@RequestMapping(value = "/qrCode/qrCodeForm", method = RequestMethod.GET)
	public String qrCodeFormGet() {
		return "study/qrCode/qrCodeForm";
	}
	
	// QR Code 개인 정보 등록폼 보기
	@RequestMapping(value = "/qrCode/qrCodeEx1", method = RequestMethod.GET)
	public String qrCodeEx1Get() {
		return "study/qrCode/qrCodeEx1";
	}
	
	// QR Code 개인 정보 QR 코드 생성처리
	@ResponseBody
	@RequestMapping(value = "/qrCode/qrCodeEx1", method = RequestMethod.POST, produces="application/text; charset=utf8")
	public String qrCodeEx1Post(HttpServletRequest request, QrCodeVO vo) {
		String realPath = request.getSession().getServletContext().getRealPath("/resources/data/qrCode/");
		String qrCodeName = studyService.setQrCodeCreate1(realPath, vo);
		return qrCodeName;
	}
	
	// QR Code 소개사이트 주소 등록폼 보기
	@RequestMapping(value = "/qrCode/qrCodeEx2", method = RequestMethod.GET)
	public String qrCodeEx2Get() {
		return "study/qrCode/qrCodeEx2";
	}
	
	// QR Code 소개사이트 QR 코드 생성처리
	@ResponseBody
	@RequestMapping(value = "/qrCode/qrCodeEx2", method = RequestMethod.POST, produces="application/text; charset=utf8")
	public String qrCodeEx2Post(HttpServletRequest request, QrCodeVO vo) {
		String realPath = request.getSession().getServletContext().getRealPath("/resources/data/qrCode/");
		String qrCodeName = studyService.setQrCodeCreate2(realPath, vo);
		return qrCodeName;
	}
	
	// QR Code 예매정보 등록폼 보기
	@RequestMapping(value = "/qrCode/qrCodeEx3", method = RequestMethod.GET)
	public String qrCodeEx3Get() {
		return "study/qrCode/qrCodeEx3";
	}
	
	// QR Code 예매정보 QR 코드 생성처리
	@ResponseBody
	@RequestMapping(value = "/qrCode/qrCodeEx3", method = RequestMethod.POST, produces="application/text; charset=utf8")
	public String qrCodeEx3Post(HttpServletRequest request, QrCodeVO vo) {
		String movieTemp = vo.getMid() + "_";
		movieTemp += vo.getMovieName() + "_";
		movieTemp += vo.getMovieDate() + "_";
		movieTemp += vo.getMovieTime() + "_A";
		movieTemp += vo.getMovieAdult() + "_C";
		movieTemp += vo.getMovieChild();
		vo.setMovieTemp(movieTemp);
		
		String realPath = request.getSession().getServletContext().getRealPath("/resources/data/qrCode/");
		String qrCodeName = studyService.setQrCodeCreate3(realPath, vo);
		return qrCodeName;
	}
	
	// QR Code 예매정보를 DB에 저장할 등록폼 보기
	@RequestMapping(value = "/qrCode/qrCodeEx4", method = RequestMethod.GET)
	public String qrCodeEx4Get() {
		return "study/qrCode/qrCodeEx4";
	}
	
	// QR Code 예매정보 QR 코드 생성 및 DB저장 처리
	@ResponseBody
	@RequestMapping(value = "/qrCode/qrCodeEx4", method = RequestMethod.POST, produces="application/text; charset=utf8")
	public String qrCodeEx4Post(HttpServletRequest request, QrCodeVO vo) {
		String movieTemp = vo.getMid() + "_";
		movieTemp += vo.getMovieName() + "_";
		movieTemp += vo.getMovieDate() + "_";
		movieTemp += vo.getMovieTime() + "_A";
		movieTemp += vo.getMovieAdult() + "_C";
		movieTemp += vo.getMovieChild();
		vo.setMovieTemp(movieTemp);
		
		String realPath = request.getSession().getServletContext().getRealPath("/resources/data/qrCode/");
		String qrCodeName = studyService.setQrCodeCreate4(realPath, vo);
		return qrCodeName;
	}
	
	// QR Code 예매정보 를 DB에서 검색하여 가져오기
	@ResponseBody
	@RequestMapping(value = "/qrCode/qrCodeSearch", method = RequestMethod.POST)
	public QrCodeVO qrCodeSearchPost(String qrCode) {
		return studyService.getQrCodeSearch(qrCode);
	}
	
	// 썸네일 연습폼 보기
	@RequestMapping(value = "/thumbnail/thumbnailForm", method = RequestMethod.GET)
	public String thumbnailFormGet() {
		return "study/thumbnail/thumbnailForm";
	}
	
	// 썸네일 이미지 처리하기
	@RequestMapping(value = "/thumbnail/thumbnailForm", method = RequestMethod.POST)
	public String thumbnailFormPost(MultipartFile file) {
		int res = studyService.setThumbnailCreate(file);
		
		if(res == 1) return "redirect:/message/thumbnailCreateOk";
		else return "redirect:/message/thumbnailCreateNo";
	}
	
	// 썸네일 이미지(study폴더의 그림파일) 모두보기
	@RequestMapping(value = "/thumbnail/thumbnailResult", method = RequestMethod.GET)
	public String thumbnailResultGet(HttpServletRequest request, Model model) {
		String realPath = request.getSession().getServletContext().getRealPath("/resources/data/thumbnail/");
		String[] files = new File(realPath).list();
		
		model.addAttribute("files", files);
		model.addAttribute("fileCount", (files.length / 2));
		
		return "study/thumbnail/thumbnailResult";
	}
	
	// thumbnail폴더의 파일 삭제하기
	@ResponseBody
	@RequestMapping(value = "/thumbnail/fileDelete", method = RequestMethod.POST)
	public String thumbnailFileDeletePost(HttpServletRequest request,
			@RequestParam(name="file", defaultValue = "", required=false) String fName) {
		String realPath = request.getSession().getServletContext().getRealPath("/resources/data/thumbnail/");
		
		int res = 0;
		File file = new File(realPath + fName);
		
		if(file.exists()) {
			file.delete();
			res = 1;
		}
		
		return res + "";
	}
	
	// thumbnail폴더의 모든 파일 삭제처리하기
	@ResponseBody
	@RequestMapping(value = "/thumbnail/fileDeleteAll", method = RequestMethod.POST)
	public String thumbnailDeleteAllPost(HttpServletRequest request,
			@RequestParam(name="file", defaultValue = "", required=false) String fName) {
		int res = 0;
		String realPath = request.getSession().getServletContext().getRealPath("/resources/data/thumbnail/");
		File targetFolder = new File(realPath);
		
		if(!targetFolder.exists()) {
      System.out.println(targetFolder + " 경로가 존재하지 않습니다.");
      return res + "";
		}
		
		File[] files = targetFolder.listFiles();
		if(files.length != 0) {
	    for(File file : files) {
	        if(!file.isDirectory()) {
	        	file.delete();
	        }
	    }
			res = 1;
		}
		return res + "";
	}
	
	// jsoup를 이용한 웹크롤링 폼보기
	@RequestMapping(value = "/crawling/jsoup", method = RequestMethod.GET)
	public String crawlingJsoupGet() {
		return "study/crawling/jsoup";
	}
	
	// jsoup를 이용한 웹크롤링 처리하기
	@ResponseBody
	@RequestMapping(value = "/crawling/jsoup", method = RequestMethod.POST)
	public ArrayList<String> crawlingJsoupPost(String url, String selector) throws Exception {
		Connection conn = Jsoup.connect(url);
		
		Document document = conn.get();
		//System.out.println("document : " + document);
		
		//Elements selects = document.select("div.cjs_t");
		//Elements selects = document.select("div.cjs_news_mw");
		//Elements selects = document.select("div.mod_vw_thumb link_thumb");
		Elements selects = document.select(selector);
		//System.out.println("selects : " + selects);
		
		ArrayList<String> vos = new ArrayList<String>();
		int i = 0;
		for(Element select : selects) {
			i++;
			//System.out.println(i + " : " + select);
			//System.out.println(i + " : " + select.text());
			System.out.println(i + " : " + select.html());
			vos.add(i + " : " + select.html());
		}
		
		return vos;
	}
	
	// jsoup를 이용한 웹크롤링 처리하기(네이버 검색어로 검색결과 처리하기)
	@ResponseBody
	@RequestMapping(value = "/crawling/naverSearch", method = RequestMethod.POST)
	public ArrayList<String> crawlingNaverSearchPost(String search, String searchSelector) throws Exception {
		Connection conn = Jsoup.connect(search);
		
		Document document = conn.get();
		Elements selects = document.select(searchSelector);
		
		ArrayList<String> vos = new ArrayList<String>();
		int i = 0;
		for(Element select : selects) {
			i++;
			System.out.println(i + " : " + select.html());
			vos.add(i + " : " + select.html());
		}
		
		return vos;
	}

	// jsoup를 이용한 웹크롤링 처리하기(네이버 이미지 검색어로 검색결과 처리하기)
	@ResponseBody
	@RequestMapping(value = "/crawling/naverImageSearch", method = RequestMethod.POST)
	public ArrayList<String> crawlingNaverImageSearchPost(String search, String searchImageSelector) throws Exception {
		Connection conn = Jsoup.connect(search);
		
		Document document = conn.get();
		System.out.println("document : " + document);
		Elements selects = document.select(searchImageSelector);
		System.out.println("searchImageSelector : " + selects);
		
		ArrayList<String> vos = new ArrayList<String>();
		int i = 0;
		for(Element select : selects) {
			i++;
			System.out.println(i + " : " + select.html());
			vos.add(i + " : " + select.html());
		}
		
		return vos;
	}
	
	
  // 크롤링폼 보기(selenium)
  @RequestMapping(value = "/crawling/selenium", method = RequestMethod.GET)
  public String crawlingSeleniumGet() {
    return "study/crawling/selenium";
  }

  // 크롤링하기(selenium)
  @ResponseBody
  @RequestMapping(value = "/crawling/selenium", method = RequestMethod.POST)
  public List<HashMap<String, Object>> crawlingSeleniumPost(HttpServletRequest request) {
    List<HashMap<String, Object>> array = new ArrayList<HashMap<String, Object>>();

    try {
      String realPath = request.getSession().getServletContext().getRealPath("/resources/crawling/");
      System.setProperty("webdriver.chrome.driver", realPath + "chromedriver.exe");


      WebDriver driver = new ChromeDriver();
      driver.get("http://www.cgv.co.kr/movies/");

      /* ---------- 응용 실습 ------------------- */

      // 현재 상영작만 보기 클릭처리
      WebElement btnMore = driver.findElement(By.id("chk_nowshow"));
      btnMore.click();


      // 더보기 버튼을 클릭했을때 더 많은 영화목록 보여주기 처리..
      btnMore = driver.findElement(By.className("link-more"));
      btnMore.click();


      // 화면이 더 열리는 동안 시간 지연시켜주기(3초)
      try {
        Thread.sleep(3000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

      // List로 처리해서 프론트로 넘겨주도록 하자.
      List<WebElement> elements = driver.findElements(By.cssSelector(".sect-movie-chart ol li"));
      for(WebElement element : elements){
        HashMap<String, Object> map = new HashMap<String, Object>();
        String image="<img src='" + element.findElement(By.tagName("img")).getAttribute("src") + "' width='300px'  />";
        String link =element.findElement(By.tagName("a")).getAttribute("href");
        String title="<a href='"+link+"' target='_blank'>" + element.findElement(By.className("title")).getText() + "</a>";
        String percent=element.findElement(By.className("percent")).getText();
        map.put("title", title);
        map.put("image", image);
        map.put("link", link);
        map.put("percent", percent);
        array.add(map);
      }

    } catch (Exception e) {
      System.out.println("CGV Crawling error : " + e.toString());
    }
    //System.out.println("array : " + array);
    return array;
  }
	
  @RequestMapping(value = "/transaction/transaction", method = RequestMethod.GET)
  public String transactionGet(Model model) {
  	List<TransactionVO> vos = studyService.getTransactionList();
  	List<TransactionVO> vos2 = studyService.getTransactionList2();
  	
  	model.addAttribute("vos", vos);
  	model.addAttribute("vos2", vos2);
  	
  	return "study/transaction/transaction";
  }
  
  // 트랜잭션을 통한 한개의 작업단위로 처리시켜준다.
  @Transactional
  @RequestMapping(value = "/transaction/transaction", method = RequestMethod.POST)
  public String transactionPost(Model model, TransactionVO vo) {
  	
  	studyService.setTransactionUser1Input(vo);
  	studyService.setTransactionUser2Input(vo);
  	
  	return "redirect:/study/transaction/transaction";
  }
  
  // user와 user2의 일괄 삽입 작업처리
  @ResponseBody
  @RequestMapping(value = "/transaction/transaction2", method = RequestMethod.POST)
//  public String transaction2Post(Model model, @Validated TransactionVO vo, BindingResult bindingResult) {
 	public String transaction2Post(Model model, 
 			String mid,
 			String name,
 			int age,
 			String address,
 			String job
 			) {
  	
//  	System.out.println("vo : " + vo);
  	System.out.println("mid : " + mid);
  	System.out.println("age : " + age);
  	
  	//if(bindingResult.hasFieldErrors()) return "redirect:/message/validateNo";
  	
  	
  	//studyService.setTransactionUserInput(vo);
  	studyService.setTransactionUserInput2(mid, name, age, address, job);
  	
  	return "redirect:/study/transaction/transaction";
  }
}
