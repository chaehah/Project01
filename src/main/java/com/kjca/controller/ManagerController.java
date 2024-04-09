package com.kjca.controller;

import java.io.IOException;
import java.util.ArrayList;

import com.kjca.dao.ManagerDAO;
import com.kjca.dto.ManagerDTO;
import com.kjca.util.PasswordUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("*.do")
public class ManagerController extends HttpServlet {
	private static final long serialVersionUID = 580028670666160861L;
	HttpSession session = null;
	private ManagerDAO dao;
	private ManagerDTO dto;
	private ArrayList<ManagerDTO> managerList;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = req.getRequestURI();
		if(path.contains("JoinForm")) {
			resp.sendRedirect("./JoinForm.jsp");
		}else if(path.contains("LoginForm")) {		
			resp.sendRedirect("./LoginForm.jsp");
		}else if(path.contains("logout")) {
			session.removeAttribute("managerId");
			System.out.println("로그아웃");
			resp.sendRedirect("./LoginForm.jsp");
		}else if (path.contains("AutorityTable")) {
			req.setAttribute("managerList", managerSelectAll(req));
			req.getRequestDispatcher("./AuthorityTable.jsp").forward(req, resp);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = req.getRequestURI();
		System.out.println(path);
		if(path.contains("joinManager")) {	//회원가입
			int result = joinManager(req);
			System.out.println(result);
			if(result>0) {	// 성공하면 index 페이지로
				resp.sendRedirect("./LoginForm.jsp");
			}else {	// 실패하면 그대로 회원가입화면 돌아가기
				resp.sendRedirect("./JoinForm.jsp");
			}
		} else if(path.contains("managerLogin")) {	//로그인
			int result = managerLogin(req);
			if(result>0) {
				resp.sendRedirect("./index.jsp");
			}else {
				resp.sendRedirect("./LoginForm.jsp");
			}
		}
	}

	// 회원가입 메소드
	private int joinManager(HttpServletRequest req) {
		String managerId = req.getParameter("managerId");
		String managerPw = PasswordUtil.encoding(req.getParameter("managerPw"));
		String managerName = req.getParameter("managerName");
		String managerEmail = req.getParameter("managerEmail");
		String managerPhone = req.getParameter("managerPhone");
		System.out.println("컨트롤러 회원가입 메서드에서의 변수들 값:"+managerId+","+managerPw);
		
		ManagerDTO dto = new ManagerDTO();
		dto.setManagerId(managerId);;
		dto.setManagerPw(managerPw);;
		dto.setManagerName(managerName);;
		dto.setManagerEmail(managerEmail);;
		dto.setManagerPhone(managerPhone);;
		
		ManagerDAO dao = new ManagerDAO();
		int result = dao.joinManager(dto);
		return result;
	}
	
	// 로그인 메소드
	private int managerLogin(HttpServletRequest req) {
		String managerId = req.getParameter("managerId");
		String managerPw = PasswordUtil.encoding(req.getParameter("managerPw"));
		
		dao = new ManagerDAO();
		dto = dao.loginManager(managerId, managerPw);
		System.out.println(dto);
		
		if(dto!=null) {
			System.out.println("로그인 성공"+dto.getManagerId());
			session = req.getSession(); 
			session.setAttribute("managerId", dto.getManagerId());
			session.setAttribute("managerFlag", dto.getManagerFlag());
			session.setAttribute("managerAdminFlag", dto.getManagerAdminFlag());
			return 1;
		}else {
			System.out.println("로그인 실패");
			return 0;
		}
	}
	
	// 매니저리스트 불러오기 메소드
	private ArrayList<ManagerDTO> managerSelectAll(HttpServletRequest req){
		dao = new ManagerDAO();
		managerList = dao.selectAllManager();
		System.out.println(managerList.get(0));
		return managerList;
	}
	
	
}
