package com.spring.javaProjectS.service;

import java.util.ArrayList;

import com.spring.javaProjectS.vo.InquiryReplyVO;
import com.spring.javaProjectS.vo.InquiryVO;
import com.spring.javaProjectS.vo.QrCodeVO;

public interface AdminService {
	
	public ArrayList<QrCodeVO> getInquiryListAdmin(int startIndexNo, int pageSize, String part);

	public InquiryVO getInquiryContent(int idx);

	public void setInquiryInputAdmin(InquiryReplyVO vo);

	public InquiryReplyVO getInquiryReplyContent(int idx);

	public void setInquiryReplyUpdate(InquiryReplyVO reVO);

	public void setInquiryReplyDelete(int reIdx);

	public void setInquiryUpdateAdmin(int inquiryIdx);

	public void setInquiryUpdateAdmin2(int inquiryIdx);

	public int setMemberLevelCheck(int idx, int level);

}
