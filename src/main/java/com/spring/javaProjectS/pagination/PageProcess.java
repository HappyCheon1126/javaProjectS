package com.spring.javaProjectS.pagination;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.javaProjectS.dao.BoardDAO;
import com.spring.javaProjectS.dao.DbShopDAO;
import com.spring.javaProjectS.dao.MemberDAO;
import com.spring.javaProjectS.dao.PdsDAO;

@Service
public class PageProcess {

	@Autowired
	BoardDAO boardDao;
	
	@Autowired
	PdsDAO pdsDAO;
	
	@Autowired
	MemberDAO memberDAO;
	
	@Autowired
	DbShopDAO dbShopDAO;
	
	public PageVO totRecCnt(int pag, int pageSize, String section, String part, String searchString) {
		PageVO pageVO = new PageVO();
		
		int totRecCnt = 0;
		String search = "";
		
		if(section.equals("board")) {
			if(part.equals(""))	totRecCnt = boardDao.totRecCnt();
			else {
				search = part;
				totRecCnt = boardDao.totRecCntSearch(search, searchString);
			}
		}
		else if(section.equals("pds")) totRecCnt = pdsDAO.totRecCnt(part);
		else if(section.equals("member"))	totRecCnt = memberDAO.totRecCnt(searchString);
		else if(section.equals("dbMyOrder")) {
			String mid = part;
			totRecCnt = dbShopDAO.totRecCnt(mid);
		}
		else if(section.equals("myOrderStatus")) {
			String mid = part;
			String[] searchStringArr = searchString.split("@");
			totRecCnt = dbShopDAO.totRecCntMyOrderStatus(mid,searchStringArr[0],searchStringArr[1],searchStringArr[2]);
		}
		else if(section.equals("adminDbOrderProcess")) {
			String[] searchStringArr = searchString.split("@");
			totRecCnt = dbShopDAO.totRecCntAdminStatus(searchStringArr[0],searchStringArr[1],searchStringArr[2]);
		}
		
		int totPage = (totRecCnt % pageSize)==0 ? (totRecCnt / pageSize) : (totRecCnt / pageSize) + 1 ;
		int startIndexNo = (pag - 1) * pageSize;
		int curScrStartNo = totRecCnt - startIndexNo;
		
		int blockSize = 3;
		int curBlock = (pag - 1) / blockSize;
		int lastBlock = (totPage - 1) / blockSize;
		
		pageVO.setPag(pag);
		pageVO.setPageSize(pageSize);
		pageVO.setTotRecCnt(totRecCnt);
		pageVO.setTotPage(totPage);
		pageVO.setStartIndexNo(startIndexNo);
		pageVO.setCurScrStartNo(curScrStartNo);
		pageVO.setBlockSize(blockSize);
		pageVO.setCurBlock(curBlock);
		pageVO.setLastBlock(lastBlock);
		pageVO.setPart(part);
		pageVO.setSearch(search);
		pageVO.setSearchString(searchString);
		
		return pageVO;
	}
}
