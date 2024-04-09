package com.kjca.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.kjca.dto.ManagerDTO;

public class ManagerDAO {
	private Connection con;			// 데이터베이스와 연결 담당
	private PreparedStatement psmt;	// 인파라미터가 있는 동적 쿼리문을 실행할 때 사용
	private ResultSet rs;			// SELECT 쿼리문의 결과를 저장할 때 사용
	
	public void getConnection() {
		try {
			Context initCtx = new InitialContext();
			Context ctx = (Context)initCtx.lookup("java:comp/env");	// lookup은 리턴타입이 Object 이므로 Context로 강제 형변환
			DataSource source = (DataSource)ctx.lookup("dbcp_mysql");
			con = source.getConnection();
			System.out.println("DB 커넥션 풀 연결 성공");
		} catch (Exception e) {
			System.out.println("DB 커넥션 풀 연결 실패");
			e.printStackTrace();
		}
	}
	
	public int joinManager(ManagerDTO dto) {
		getConnection();
		String query = "INSERT INTO manager(managerId,managerPw,managerName,managerPhone,managerEmail) VALUES(?,?,?,?,?)";
		int result = 0;
		try {
			psmt = con.prepareStatement(query);
			psmt.setString(1, dto.getManagerId());
			psmt.setString(2, dto.getManagerPw());
			psmt.setString(3, dto.getManagerName());
			psmt.setString(4, dto.getManagerPhone());
			psmt.setString(5, dto.getManagerEmail());
			result = psmt.executeUpdate();
		} catch (SQLException e) {
			e.getStackTrace();
		} finally {
			close();
		}
		System.out.println("dao 성공");
		return result;
	}
	
	public ManagerDTO loginManager(String managerId,String managerPw) {
		getConnection();
		ManagerDTO dto = null;
		String query = "SELECT * FROM manager WHERE managerId=? AND managerPw=?";
		try {
			psmt = con.prepareStatement(query);
			psmt.setString(1,managerId);
			psmt.setString(2,managerPw);
			ResultSet rs = psmt.executeQuery();
			
			while(rs.next()) {
				dto = new ManagerDTO();
				dto.setManagerId(rs.getString("managerId"));
				dto.setManagerFlag(rs.getInt("managerFlag"));
				dto.setManagerAdminFlag(rs.getInt("managerAdminFlag"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return dto;
	}
	
	public ArrayList<ManagerDTO> selectAllManager(){
		getConnection();
		ArrayList<ManagerDTO> managerList = new ArrayList<ManagerDTO>();
		String query = "SELECT * FROM manager ORDER BY managerNo";
		try {
			psmt = con.prepareStatement(query);
			rs = psmt.executeQuery();
			while(rs.next()) {
				ManagerDTO dto = new ManagerDTO();
				dto.setManagerNo(rs.getInt("managerNo"));
				dto.setManagerId(rs.getString("managerId"));
				dto.setManagerName(rs.getString("managerName"));
				dto.setManagerEmail(rs.getString("managerEmail"));
				dto.setManagerPhone(rs.getString("managerPhone"));
				dto.setManagerFlag(rs.getInt("managerFlag"));
				dto.setManagerAdminFlag(rs.getInt("managerAdminFlag"));
				managerList.add(dto);
			}
		} catch (Exception e) {
			System.out.println("매니저리스트 조회중 예외발생");
			e.printStackTrace();
		} finally {
			close();
		}
		
		return managerList;
	}
	
//	public ManagerDTO selectUser(int userNum) {
//		getConnection();
//		ManagerDTO dto = null;
//		String query = "SELECT * FROM user WHERE userNum=?";
//		try {
//			psmt = con.prepareStatement(query);
//			psmt.setInt(1,userNum);
//			ResultSet rs = psmt.executeQuery();
//			
//			while(rs.next()) {
//				dto = new ManagerDTO();
//				dto.setUserNum(rs.getInt("userNum"));
//				dto.setUserId(rs.getString("userId"));
//				dto.setUserPw(rs.getString("userPw"));
//				dto.setUserName(rs.getString("userName"));
//				dto.setUserAge(rs.getInt("userAge"));
//				dto.setUserEmail(rs.getString("userEmail"));
//				dto.setUserPhone(rs.getString("userPhone"));
//				dto.setUserFile(rs.getString("userFile"));
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//			System.out.println("유저 한명 조회 오류");
//		} finally {
//			close();
//		}
//		return dto;
//	}
//
//	public int checkUserId(String userId) {
//		getConnection();
//		ManagerDTO dto = null;
//		String query = "SELECT * FROM user WHERE userId=?";
//		int result = 0;
//		try {
//			psmt = con.prepareStatement(query);
//			psmt.setString(1,userId);
//			ResultSet rs = psmt.executeQuery();
//			
//			while(rs.next()) {
//				result++;
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//			System.out.println("유저다오:아이디 중복체크 조회 오류");
//		} finally {
//			close();
//		}
//		return result;
//	}
//
//	public int updateUser(ManagerDTO dto) {
//		getConnection();
//		String query = "UPDATE user SET userPw=?,userName=?,userAge=?,userEmail=?,userPhone=?,userFile=? WHERE userNum=?";
//		int result = 0;
//		try {
//			psmt = con.prepareStatement(query);
//			psmt.setString(1, dto.getUserPw());
//			psmt.setString(2, dto.getUserName());
//			psmt.setInt(3, dto.getUserAge());
//			psmt.setString(4, dto.getUserEmail());
//			psmt.setString(5, dto.getUserPhone());
//			psmt.setString(6, dto.getUserFile());
//			psmt.setInt(7, dto.getUserNum());
//			result = psmt.executeUpdate();
//			System.out.println("성공여부"+result);
//		} catch (SQLException e) {
//			e.getStackTrace();
//			System.out.println("유저업데이트 실패");
//		} finally {
//			close();
//		}
//		return result;
//	}
//	
//	public int deleteUser(int userNum) {
//		getConnection();
//		String query = "DELETE FROM user WHERE userNum=?";
//		int result = 0;
//		try {
//			psmt = con.prepareStatement(query);
//			psmt.setInt(1, userNum);
//			result = psmt.executeUpdate();
//		} catch (SQLException e) {
//			e.getStackTrace();
//			System.out.println("삭제 실패");
//		} finally {
//			close();
//		}
//		return result;
//	}
	
	public void close() {	// close를 안 하면 자원낭비가 됨.
		try {
			if(rs != null) rs.close();
			if(psmt != null) psmt.close();
			if(con != null) con.close();
			
			System.out.println("DB 커넥션 풀 자원 반납");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
