package com.spring.javaProjectS.dao;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Param;

import com.spring.javaProjectS.vo.InquiryReplyVO;
import com.spring.javaProjectS.vo.InquiryVO;
import com.spring.javaProjectS.vo.QrCodeVO;

public interface AdminDAO {

	public ArrayList<QrCodeVO> getInquiryListAdmin(@Param("startIndexNo") int startIndexNo, @Param("pageSize") int pageSize, @Param("part") String part);

	public InquiryVO getInquiryContent(@Param("idx") int idx);

	public void setInquiryInputAdmin(@Param("vo") InquiryReplyVO vo);

	public InquiryReplyVO getInquiryReplyContent(@Param("idx") int idx);

	public void setInquiryReplyUpdate(@Param("reVO") InquiryReplyVO reVO);

	public void setInquiryReplyDelete(@Param("reIdx") int reIdx);

	public void setInquiryUpdateAdmin(@Param("inquiryIdx") int inquiryIdx);

	public int totRecCntAdmin(@Param("part") String part);

	public void setInquiryUpdateAdmin2(@Param("inquiryIdx") int inquiryIdx);

	public int setMemberLevelCheck(@Param("idx") int idx, @Param("level") int level);
	
}
