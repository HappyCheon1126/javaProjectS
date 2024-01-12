package com.spring.javaProjectS.service;

import java.util.List;

import com.spring.javaProjectS.vo.ScheduleVO;

public interface ScheduleService {

	public void getSchedule();

	public List<ScheduleVO> getScheduleMenu(String mid, String ymd);

	public void setScheduleInputOk(ScheduleVO vo);

	public void setScheduleUpdateOk(ScheduleVO vo);

	public void setScheduleDeleteOk(int idx);

}
