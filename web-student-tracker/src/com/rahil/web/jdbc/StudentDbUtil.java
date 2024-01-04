package com.rahil.web.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

public class StudentDbUtil {

	private DataSource dataSource;
	
	public StudentDbUtil(DataSource theDataSource) {
		dataSource = theDataSource;
	}
	
	public List<Student> getStudents() throws Exception{
		List<Student> students = new ArrayList<>();
		
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRes = null;
		
		try {
		//get a connection
			myConn = dataSource.getConnection();
		
		//create sql statement
			String sql = "select * from student order by last_name";
		
			myStmt = myConn.createStatement();
		//execute query 
			myRes = myStmt.executeQuery(sql);
		
		//process result set
			while(myRes.next()) {
				//retrieve data from result set row 
				int id = myRes.getInt("id");
				String firstName = myRes.getString("first_name");
				String lastName = myRes.getString("last_name");
				String email = myRes.getString("email");
				
				Student tempStudent = new Student(id, firstName, lastName, email);
				students.add(tempStudent);
			}
	
			return students;
		}finally {
			//close JDBC objects
			close(myConn, myStmt, myRes);
		}
		
		
		
	}

	private void close(Connection myConn, Statement myStmt, ResultSet myRes) {
		try {
			if(myRes != null) {
				myRes.close();
			}
			if(myStmt != null) {
				myStmt.close();
			}
			if(myConn != null) {
				myConn.close();
			}
			
		}catch(Exception e){
				e.printStackTrace();
		}
	
	}

	public void addStudent(Student theStudent)throws Exception {
		
		Connection myConn = null;
		PreparedStatement myStmt = null;
		
		try {
		//get DB connection
		myConn = dataSource.getConnection();
		//create sql statement
		String sql = "insert into student"
					+"(first_name, last_name, email)"
					+"values(?, ?, ?)";
		
		myStmt = myConn.prepareStatement(sql);
		//set the param values for the student
		myStmt.setString(1, theStudent.getFirstName());
		myStmt.setString(2, theStudent.getLastName());
		myStmt.setString(3, theStudent.getEmail());
		//execute sql statement
		myStmt.execute();
		}finally {
		//clean up JDBC objects
			close(myConn, myStmt, null);
		}
	}

	public Student getStudent(String theStudentId)throws Exception {
		Student theStudent = null;
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		int studentId;
		
		try {
			//convert student id to int
			studentId = Integer.parseInt(theStudentId);
			
			//get connection to database
			myConn = dataSource.getConnection();
			
			//create sql to get selected student
			String sql = "select * from student where id = ?";
			
			// create prepared statement
			myStmt = myConn.prepareStatement(sql);
			
			//set params
			myStmt.setInt(1, studentId);
			
			//execute statement
			myRs = myStmt.executeQuery();
			
			//retrieve data from result set row
			if(myRs.next()) {
				String firstName = myRs.getString("first_name");
				String lastName = myRs.getString("last_name");
				String email = myRs.getString("email");
				
				theStudent = new Student(studentId, firstName, lastName, email);
			}else {
				throw new Exception("Could not find student id: "+ studentId);
			}
		}finally {
			//close JDBC objects;
			close(myConn, myStmt, myRs);
		}
		return theStudent;
	}

	public void updateStudent(Student theStudent)throws Exception {
		Connection myConn = null;
		
		PreparedStatement myStmt = null;
		try{
			
			
			//get db Connection
			myConn = dataSource.getConnection();
			
			//create SQL update statement
			String sql = "update student " + "set first_name = ?, last_name = ?, email = ? " + "where id = ?";
			
			//prepare statement
			myStmt = myConn.prepareStatement(sql);
			
			//set params
			myStmt.setString(1, theStudent.getFirstName());
			myStmt.setString(2, theStudent.getLastName());
			myStmt.setString(3, theStudent.getEmail());
			myStmt.setInt(4, theStudent.getId());
			
			//execute SQL statement
			myStmt.execute();
		}finally {
			//clean up JDBC objects
			close(myConn, myStmt, null);
		}
	}

	public void deleteStudent(String theStudentId)throws Exception {
		
		Connection myConn = null;
		PreparedStatement myStmt = null;
		int studentId;
		try {
			//convert student id to int
			studentId = Integer.parseInt(theStudentId);
			
			//get connection to database
			myConn = dataSource.getConnection();
			
			//create sql to delete student
			String sql = "delete from student where id = ?";
			
			//prepare statement
			myStmt = myConn.prepareStatement(sql);
			
			//set params
			myStmt.setInt(1, studentId);
			
			//execute sql statement
			myStmt.execute();
		}finally {
			//clean up JDBC code
			close(myConn,myStmt, null);
		}
		
	}

	public List<Student> searchStudents(String theSearchName)throws Exception {
		
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		List<Student> students = new ArrayList<>();
		int studentId; 
		try {
			//set up connection to database
			myConn = dataSource.getConnection();
			
			if(theSearchName != null && theSearchName.trim().length()>0) {
				//create a sql statement
				String sql = "select * from student where lower(first_name) like ? or lower(last_name) like ?";
				
				String theSearchNameLike = "%"+theSearchName.toLowerCase()+"%";
				//prepare statement
				myStmt = myConn.prepareStatement(sql);
				myStmt.setString(1, theSearchNameLike);
				myStmt.setString(2, theSearchNameLike);
				//execute the query
				
				
				
			}else {
				String sql = "select * from student order by last_name";
				myStmt = myConn.prepareStatement(sql);
			}
			myRs = myStmt.executeQuery();
			while(myRs.next()) {
				String firstName = myRs.getString("first_name");
				String lastName = myRs.getString("last_name");
				String email = myRs.getString("email");
				studentId = myRs.getInt("id");
				
				Student tempStudent = new Student(studentId, firstName, lastName, email);
				students.add(tempStudent);
			}
			return students;
		}finally {
			//clean up JDBC code
			close(myConn, myStmt, myRs);
		}
		
	}
}
