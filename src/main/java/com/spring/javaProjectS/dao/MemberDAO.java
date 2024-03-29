package com.spring.javaProjectS.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.spring.javaProjectS.vo.MemberVO;

public interface MemberDAO {

	public MemberVO getMemberIdCheck(@Param("mid") String mid);

	public MemberVO getMemberNickCheck(@Param("nickName") String nickName);

	public int setMemberJoinOk(@Param("vo") MemberVO vo);

	public int setUserDel(@Param("mid") String mid);

	public int setPwdChangeOk(@Param("mid") String mid, @Param("pwd") String pwd);

	public int setMemberUpdateOk(@Param("vo") MemberVO vo);

	public void setMemberPasswordUpdate(@Param("mid") String mid, @Param("pwd") String pwd);

	public List<MemberVO> getMemberEmailSearch(@Param("email") String email);

	public MemberVO getMemberNickNameEmailCheck(@Param("nickName") String nickName, @Param("email") String email);

	public void setKakaoMemberInput(@Param("mid") String mid, @Param("pwd") String pwd, @Param("nickName") String nickName, @Param("email") String email);

	public List<MemberVO> getMemberList(@Param("startIndexNo") int startIndexNo, @Param("pageSize") int pageSize, @Param("mid") String mid);

	public int totRecCnt(@Param("mid") String mid);

}
