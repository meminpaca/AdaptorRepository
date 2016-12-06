/**
 * 
 */
package com.adaptor.uni.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.adaptor.uni.App;
import com.adaptor.uni.course.ICourse;
import com.adaptor.uni.course.ICourseSection;
import com.adaptor.uni.model.IDepartment;
import com.adaptor.uni.model.IUniversity;
import com.adaptor.uni.model.TUniversity;
import com.adaptor.uni.utils.TAdaptorConstants;
import com.adaptor.uni.utils.THelper;
import com.adaptor.uni.utils.TUniversityCourseSectionTypes;

/**
 * @author emin.paca
 *
 */
public class TUniversityDBManager {
	/**
	 * Logger for logging  events
	 * */
	public static  final Logger logger = LogManager.getLogger(TUniversityDBManager.class);

	
	
	private static final String insertDepartmentSQL = "INSERT INTO Department"
			+ "(FacultyID, Code, Name , Description, isActive) VALUES"
			+ "(?,?,?,?,?)";


	private static final String insertCourseSQL = "INSERT INTO Course"
			+ "(DepartmentID, Code, Name , Description, IsDeleted,Status) VALUES"
			+ "(?,?,?,?,?,?)";

	private static final String insertInstructorSQL = "INSERT INTO Instructor"
			+ "(DepartmentID, Code, Name, Surname , Description, DuplicatedID,isActive) VALUES"
			+ "(?,?,?,?,?,?,?)";
	
	private static final String insertCourseSectionSQL = "INSERT INTO Section"
			+ "(CourseID, SemesterID, InstructorID,Code, Description, Credit, CreditECTS, Type,"
			+ "Quota, CurrentQuota, isActive,HasLabOrPs) VALUES"
			+ "(?,?,?,?,?,?,?,?,?,?,?,?)";
	
	private static final String insertSectionDetailSQL = "INSERT INTO SectionDetail"
			+ "(SectionID, DayID, TimeTableID,Room, IsActive) "
			+ "VALUES(?,?,?,?,?)";
	

	private static final String deleteCourseSectionDetailSQL = "DELETE FROM SectionDetail WHERE SectionID=?";
	

	private static final String departmentIDSQL = "SELECT DepartmentID FROM Department WHERE FacultyID=?"
			+ " AND Name=? AND Code=? AND isActive=?";
	
	private static final String courseIDSQL = "SELECT CourseID FROM Course WHERE DepartmentID=?"
			+ " AND Code=?";
	
	private static final String instructorIDSQL = "SELECT InstructorID FROM Instructor WHERE DepartmentID=?"
			+ " AND Code=?";
	
	private static final String sectionIDSQL = "SELECT SectionID FROM Section WHERE CourseID=?"
			+ " AND SemesterID=? AND InstructorID=? AND Code=? AND Type=?";

	private static final String updateDepartmentSQL = "UPDATE Department SET "
			+ "Code=? WHERE DepartmentID=?";

	
	private boolean isInsertDepartmentEnabled = true;
	/**
	 * Save.
	 *
	 * @param universityObj the university object
	 * @return true, if successful
	 */
	public boolean save(IUniversity universityObj) {
		if (universityObj == null) {
			return false;
		}
		
		List<IDepartment> departments = universityObj.getDepartments();
		if (departments == null) {
			logger.error("Department list is empty (" + universityObj.getUniversityName() + ")");
		}
		saveDepartments(universityObj,departments);		
		return true;
	}

	/**
	 * @param departments
	 */
	private void saveDepartments(IUniversity universityObj,List<IDepartment> departments) {
		Iterator<IDepartment> depIt = departments.iterator();
		while (depIt.hasNext()) {
			IDepartment currentDep = depIt.next();
			
			if (isInsertDepartmentEnabled) {
				try {
					insertOrUpdateDepartment(universityObj, currentDep);
				} catch(SQLException e) {
					logger.error("Failed to insert into database " + currentDep.getDepartmentName() + " department." );
				}
			}
			
			int departmentId = 0;
			try {
				departmentId = queryDepartmentID(currentDep);
			} catch (SQLException e) {
				logger.error("Failed to query department id on database: " + currentDep.getDepartmentName() + " department." );
			}
			if (departmentId == 0) {
				logger.error(currentDep.getDepartmentCode() + "-" + currentDep.getDepartmentName() 
				+ " department is not exist in DB.It can be a new department");
				System.out.println();
			}
//			saveCourses(universityObj.getSemesterID(),departmentId,currentDep.getCourseMap());
		}

	}

	/**
	 * @param departmentId
	 * @param courseMap
	 */
	private void saveCourses(int semesterId, int departmentId, Map<String, ICourse> courseMap) {
		if (courseMap == null) {
			return;
		}
		
		Collection<ICourse> courses = courseMap.values();
		Iterator<ICourse> courseIt = courses.iterator();
		while (courseIt.hasNext()) {
			ICourse course = courseIt.next();
			boolean result = insertOrUpdateCourse(departmentId,course);
			if (result) {
				logger.info(course.getName() + " course committed.");
			} 
			
			
			int courseId = 0;
			try {
				courseId = queryCourseID(departmentId,course);
			} catch (SQLException e) {
				logger.error("Failed to query course id on database: " + course.getName() + " course." );
			}
			saveSections(semesterId,departmentId, courseId, course.getCourseSections());
		}
		
		
		
		
	}

	
	/**
	 * @param departmentId
	 * @param courseId
	 * @param courseSections
	 */
	private void saveSections(int semesterId, int departmentId, int courseId, List<ICourseSection> courseSections) {
		if (courseSections == null) {
			return;
		}
		
		if (courseId==0) {
			System.out.println("Course id is still zero");
		}
		
		Iterator<ICourseSection> courseSecIt = courseSections.iterator();
		
		while (courseSecIt.hasNext()) {
			ICourseSection next = courseSecIt.next();
			
			// get instructor id or create a new instructor
			int instructorId = 0;
			try {
				instructorId = getIDOfInstructor(departmentId,courseId,next);
				
				if (instructorId == 0) {
					if (insertInstructor(departmentId,courseId,next)) {
						instructorId = getIDOfInstructor(departmentId,courseId,next);
						System.err.println(next.getInstructorName() + " instructor inserted.");
					}
				}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// proceed section
			// query by courseId, semesterId, instructorId and section code
			int sectionId = 0;
			try {
				sectionId = querySectionId(courseId,semesterId,instructorId,next.getCodeSec(),next.getType());
			} catch (SQLException e) {
				logger.error("SQLException ccured while querying section id " + next.getCodeSec(),e);
			}
			// if not exist
			if (sectionId == 0) {
				try {
					if (insertSection(courseId, semesterId, instructorId, next.getCodeSec()
							,next.getCourseSectionDescription()
							,next.getCredit()
							,next.getEcts()
							,next.getType()
							,next.hasLabOrPs())) {
						System.out.println(next.getCodeSec() + " section successfully inserted to the DB");
						
					}
				} catch (SQLException e) {
					logger.error("SQLEXception occured while inserting " + next.getCodeSec() + " section.",e);
				}
				
			} else {	// update it
				System.err.println("Update section ... " + next.getCodeSec());
			}
			
			if (sectionId == 0) {
				try {
					sectionId = querySectionId(courseId,semesterId,instructorId,next.getCodeSec(),next.getType());
				} catch (SQLException e) {
					logger.error("SQLException ccured while querying section id " + next.getCodeSec(),e);
				}
			}
			if (sectionId != 0) {
				saveSectionDetails(sectionId,next);
			}
			
		}
	}

	/**
	 * @param sectionId
	 * @param next
	 */
	private void saveSectionDetails(int sectionId, ICourseSection next) {

		boolean delResult = false;
		try {
			 delResult = deleteAllSectionDetails(sectionId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (delResult) {
			saveSectionDetailsForDay(sectionId,next);
		}
		
	}

	/**
	 * @param sectionId
	 * @param next
	 */
	private void saveSectionDetailsForDay(int sectionId, ICourseSection next) {
		List<String> courseDays = next.getDays();
		List<String> hours = next.getHours();
		List<String> rooms = next.getRooms();
		
		for (int i = 0; i < courseDays.size(); ++i) {
			try {
				String room = rooms.size() > i ? rooms.get(i) : TAdaptorConstants.EMPTY_STRING;
				String hour = hours.size() > i ? hours.get(i) : TAdaptorConstants.EMPTY_STRING;
				insertSectionDetails(sectionId,next,courseDays.get(i),hour,room);
			} catch (SQLException e) {
				logger.error("Exception occured while inserting course section detail.",e);
			} catch(IndexOutOfBoundsException ex) {
				logger.error("Index out of exception occured while inserting course section detail",ex);
			}			
			
		}
		
	}

	/**
	 * @param sectionId
	 * @param next
	 * @param day 
	 * @throws SQLException 
	 */
	private boolean insertSectionDetails(int sectionId, ICourseSection next, String day,String timeTableID,String room) throws SQLException {
		TDatabaseConn instance = TDatabaseConn.getInstance();
		boolean result;
		if (instance == null | instance.getConnection() == null){
			logger.error("Database connection failed and inserting section detail cannot be done.");
		}
		
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;

		try {
			dbConnection = instance.getConnection();
			preparedStatement = dbConnection.prepareStatement(insertSectionDetailSQL);
			preparedStatement.setInt(1, sectionId);
			preparedStatement.setInt(2, queryDayID(day));
			preparedStatement.setInt(3, THelper.castToInt(timeTableID));
			preparedStatement.setString(4, room);
			preparedStatement.setBoolean(5, true);
			// execute insert SQL stetement
			preparedStatement.executeUpdate();
			result = true;
			logger.info(sectionId  + " section detail successfully inserted.");
		} catch (SQLException e) {
			logger.error("SQL exception occured while inserting course section detail to the DB. ",e);
			result = false;
		} finally {

			if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}
		}
		return result;
	}

	/**
	 * @param day
	 * @return
	 */
	private int queryDayID(String day) {
		return TDayDBUtility.queryDayID(day);
	}

	/**
	 * @throws SQLException 
	 * 
	 */
	private boolean deleteAllSectionDetails(int sectionID) throws SQLException {
		TDatabaseConn instance = TDatabaseConn.getInstance();
		boolean result;
		if (instance == null | instance.getConnection() == null){
			logger.error("Database connection failed and deleting course section detail cannot be done.");
		}
		
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;

		try {
			dbConnection = instance.getConnection();
			preparedStatement = dbConnection.prepareStatement(deleteCourseSectionDetailSQL);
			preparedStatement.setInt(1, sectionID);
			// execute delete SQL stetement
			preparedStatement.executeUpdate();
			result = true;
		} catch (SQLException e) {
			logger.error("SQL exception occured while deleting course section detail on DB",e);
			result = false;
		} finally {

			if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}
		}
		return result;
	}
	

	/**
	 * @param courseId
	 * @param semesterId
	 * @param instructorId
	 * @param codeSec
	 * @param courseSectionDescription
	 * @param credit
	 * @param ects
	 * @param type
	 * @return
	 * @throws SQLException 
	 */
	private boolean insertSection(int courseId, int semesterId, int instructorId, String codeSec,
			String courseSectionDescription, String credit, String ects, String type,boolean hasLabOrPs) throws SQLException {
		TDatabaseConn instance = TDatabaseConn.getInstance();
		boolean result;
		if (instance == null | instance.getConnection() == null){
			logger.error("Database connection failed and inserting course section cannot be done.");
		}
		
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;

		try {
			dbConnection = instance.getConnection();
			preparedStatement = dbConnection.prepareStatement(insertCourseSectionSQL);

			preparedStatement.setInt(1, courseId);
			preparedStatement.setInt(2, semesterId);
			preparedStatement.setInt(3, instructorId);
			preparedStatement.setString(4, codeSec);
			preparedStatement.setString(5, courseSectionDescription);
			preparedStatement.setInt(6, convertInt(credit));
			preparedStatement.setInt(7, convertInt(ects));
			preparedStatement.setString(8, type);
			preparedStatement.setNull(9,  java.sql.Types.INTEGER);
			preparedStatement.setNull(10,  java.sql.Types.INTEGER);
			preparedStatement.setBoolean(11, true);
			preparedStatement.setBoolean(12, hasLabOrPs);

			// execute insert SQL stetement
			preparedStatement.executeUpdate();
			result = true;
		} catch (SQLException e) {
			logger.error("SQL exception occured while inserting course to the DB",e);
			result = false;
		} finally {

			if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}

		}
		return result;
	}
	

	public int convertInt(String s) {
		if (s == null || "".equals(s)) {
			return 0;
		}
		int result = 0;
		
		try {
			result = new Double(s).intValue();
			 
		} catch(NumberFormatException nfe) {
			result = 0;
		}
		
		return result;
	}

	/**
	 * @param courseId
	 * @param semesterId
	 * @param instructorID
	 * @param codeSec
	 * @return
	 * @throws SQLException 
	 */
	private int querySectionId(int courseId, int semesterId, int instructorId, String codeSec,String type) throws SQLException {
		TDatabaseConn instance = TDatabaseConn.getInstance();
		if (instance == null | instance.getConnection() == null){
			logger.error("Database connection failed and querying section id cannot be done.");
		}
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;
		
		int result = 0;
		try {
			dbConnection = instance.getConnection();
			preparedStatement = dbConnection.prepareStatement(sectionIDSQL);

			preparedStatement.setInt(1, courseId);
			preparedStatement.setInt(2, semesterId);
			preparedStatement.setInt(3, instructorId);
			preparedStatement.setString(4, codeSec);
			preparedStatement.setString(5, type);

			// execute insert SQL stetement
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				result = resultSet.getInt("SectionID");
			}

		} catch (SQLException e) {
			logger.error("SQL exception occured.",e);
		} finally {
			if (preparedStatement != null) {
				preparedStatement.close();
			}
			if (dbConnection != null) {
				dbConnection.close();
			}
		}
		return result;
	}

	/**
	 * @param departmentId
	 * @param courseId
	 * @param next
	 * @throws SQLException 
	 */
	private boolean insertInstructor(int departmentId, int courseId, ICourseSection next) throws SQLException {

		TDatabaseConn instance = TDatabaseConn.getInstance();
		boolean result;
		if (instance == null | instance.getConnection() == null){
			logger.error("Database connection failed and inserting instructor cannot be done.");
		}
		
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;

		try {
			dbConnection = instance.getConnection();
			preparedStatement = dbConnection.prepareStatement(insertInstructorSQL);

			preparedStatement.setInt(1, departmentId);
			preparedStatement.setString(2, next.getInstructorCode());
			preparedStatement.setString(3, next.getInstructorName());
			preparedStatement.setString(4, next.getInstructorSurname());
			preparedStatement.setString(5, "desc");
			preparedStatement.setNull(6,  java.sql.Types.INTEGER);
			preparedStatement.setBoolean(7, true);

			// execute insert SQL stetement
			preparedStatement.executeUpdate();
			System.out.println(next.getInstructorName() + " instructor inserted to the DB");
			result = true;
		} catch (SQLException e) {
			logger.error("SQL exception occured while inserting course to the DB",e);
			result = false;
		} finally {

			if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}

		}
		return result;
	}

	/**
	 * @param departmentId
	 * @param courseId
	 * @param next
	 * @return
	 * @throws SQLException 
	 */
	private int getIDOfInstructor(int departmentId, int courseId, ICourseSection courseSec) throws SQLException {
		TDatabaseConn instance = TDatabaseConn.getInstance();
		if (instance == null | instance.getConnection() == null){
			logger.error("Database connection failed and querying instructor id cannot be done.");
		}
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;
		
		int result = 0;
		try {
			dbConnection = instance.getConnection();
			preparedStatement = dbConnection.prepareStatement(instructorIDSQL);

			preparedStatement.setInt(1, departmentId);
			preparedStatement.setString(2, courseSec.getInstructorCode());

			// execute insert SQL stetement
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				result = resultSet.getInt("InstructorID");
			}

		} catch (SQLException e) {
			logger.error("SQL exception occured.",e);
		} finally {
			if (preparedStatement != null) {
				preparedStatement.close();
			}
			if (dbConnection != null) {
				dbConnection.close();
			}
		}
		return result;
	}

	/**
	 * Query course ID.
	 *
	 * @param departmentId the department id
	 * @param course the course
	 * @return the int
	 * @throws SQLException the SQL exception
	 */
	private int queryCourseID(int departmentId, ICourse course) throws SQLException {
		
		TDatabaseConn instance = TDatabaseConn.getInstance();
		if (instance == null | instance.getConnection() == null){
			logger.error("Database connection failed and querying course id cannot be done.");
		}
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;
		
		int result = 0;
		try {
			dbConnection = instance.getConnection();
			preparedStatement = dbConnection.prepareStatement(courseIDSQL);

			preparedStatement.setInt(1, departmentId);
			preparedStatement.setString(2, course.getCourseCode());

			// execute insert SQL stetement
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				result = resultSet.getInt("CourseID");
			}

			System.out.println("Course id was queried: " + result);

		} catch (SQLException e) {
			logger.error("SQL exception occured.",e);
		} finally {
			if (preparedStatement != null) {
				preparedStatement.close();
			}
			if (dbConnection != null) {
				dbConnection.close();
			}
		}
		return result;
	}

	/**
	 * İnsert or update course.
	 *
	 * @param departmentId the department id
	 * @param course the course
	 * @return true, if successful
	 */
	private boolean insertOrUpdateCourse(int departmentId, ICourse course) {
		
		int courseID = 0;
		
		try {
			courseID = getCourseIDFromDB(departmentId,course);
		} catch (SQLException e) {
			logger.error("Getting course id from database failed.",e);
		}
		
		// this course is not exist in DB -> insert it
		if (courseID == 0)  {
			boolean result = false;
			try {
				result = insertCourseToDB(departmentId,course);
			} catch (SQLException e) {
				logger.error("SQLException occured while inserting course to the DB: " + course.getName() + " course." );
			}
			
			if (result) {
				logger.info(course.getName() + " course successfully inserted to the DB");
				return true;
			} else {
				logger.error("Failed to insert database " + course.getName() + " course. ");
				return false;
			}
					
		} else {	// this course is exist in DB-> just update it
			logger.info(course.getName() + " is already exist in database. It should be updated.");
			return false;
		}
		
	}

	/**
	 * @param departmentId
	 * @param course
	 * @return
	 * @throws SQLException 
	 */
	private boolean insertCourseToDB(int departmentId, ICourse course) throws SQLException {
		TDatabaseConn instance = TDatabaseConn.getInstance();
		boolean result;
		if (instance == null | instance.getConnection() == null){
			logger.error("Database connection failed and inserting course cannot be done.");
		}
		
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;

		

		try {
			dbConnection = instance.getConnection();
			preparedStatement = dbConnection.prepareStatement(insertCourseSQL);

			preparedStatement.setInt(1, departmentId);
			preparedStatement.setString(2, course.getCourseCode());
			preparedStatement.setString(3, course.getName());
			preparedStatement.setString(4, course.getName());
			preparedStatement.setBoolean(5, false);
			preparedStatement.setInt(6, 1);	// 0->pasif 1-> aktif 2-> aktif ama guncellenecek

			// execute insert SQL stetement
			preparedStatement.executeUpdate();
			result = true;
		} catch (SQLException e) {
			logger.error("SQL exception occured while inserting course to the DB",e);
			result = false;
		} finally {

			if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}

		}
		return result;
	}

	/**
	 * @param departmentId
	 * @param course
	 * @return
	 * @throws SQLException 
	 */
	private int getCourseIDFromDB(int departmentId, ICourse course) throws SQLException {
		String courseIDSQL = "SELECT courseID FROM Course WHERE departmentID=?"
				+ " AND Code=?";

		TDatabaseConn instance = TDatabaseConn.getInstance();

		if (instance == null | instance.getConnection() == null){
			logger.error("Database connection failed and getting course id cannot be done.");
		}

		
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;

		
		int result = 0;
		try {
			dbConnection = instance.getConnection();
			preparedStatement = dbConnection.prepareStatement(courseIDSQL);

			preparedStatement.setInt(1, departmentId);
			preparedStatement.setString(2, course.getCourseCode());

			// execute insert SQL stetement
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				result = resultSet.getInt("CourseID");
			}

			System.out.println("Course id was queried: " + result);

		} catch (SQLException e) {
			logger.error("SQL exception occured.",e);
		} finally {
			if (preparedStatement != null) {
				preparedStatement.close();
			}
			if (dbConnection != null) {
				dbConnection.close();
			}
		}
		return result;
	}

	/**
	 * @param currentDep
	 * @return
	 * @throws SQLException 
	 */
	private int queryDepartmentID(IDepartment currentDep) throws SQLException {
		

		TDatabaseConn instance = TDatabaseConn.getInstance();

		if (instance == null | instance.getConnection() == null){
			logger.error("Database connection failed and inserting departmen cannot be done.");
		}

		
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;

		
		int result = 0;
		try {
			dbConnection = instance.getConnection();
			preparedStatement = dbConnection.prepareStatement(departmentIDSQL);

			preparedStatement.setInt(1, currentDep.getFacultyID());
			preparedStatement.setString(2, currentDep.getDepartmentName());
			preparedStatement.setString(3, currentDep.getDepartmentCode());
			preparedStatement.setBoolean(4, true);

			// execute insert SQL stetement
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				result = resultSet.getInt("DepartmentID");
			}

			System.out.println("Department id was queried: " + result);

		} catch (SQLException e) {
			logger.error("SQL exception occured.",e);
		} finally {
			if (preparedStatement != null) {
				preparedStatement.close();
			}
			if (dbConnection != null) {
				dbConnection.close();
			}
		}
		return result;
	}

	/**
	 * @param universityObj
	 * @param currentDep
	 * @throws SQLException 
	 */
	private void insertOrUpdateDepartment(IUniversity universityObj, IDepartment currentDep) throws SQLException {
		
		
		// query department id
		int departmentId = 0;
		try {
			departmentId = queryDepartmentID(currentDep);
		} catch (SQLException e) {
			logger.error("Failed to query department id on database: " + currentDep.getDepartmentName() + " department." );
		}
		
		if (departmentId == 0) {
			logger.error(currentDep.getDepartmentCode() + "-" + currentDep.getDepartmentName() 
			+ " department is not exist in DB.It is being inserted.");

			insertNewDepartment(currentDep);

		} else {
			boolean result = updateDepartment(departmentId,currentDep);
			if (result) {
				logger.info(currentDep.getDepartmentName() + " is updated.");
			}else {
				logger.error("Failed to update " + currentDep + " department.");
				
			}
			
		}
		
		
		// if not exist 
			// create new one
		// else 
			// just update it
		


	}

	/**
	 * @param departmentId 
	 * @param currentDep
	 * @return
	 * @throws SQLException 
	 */
	private boolean updateDepartment(int departmentId, IDepartment currentDep) throws SQLException {
		
		TDatabaseConn instance = TDatabaseConn.getInstance();
		
		if (instance == null | instance.getConnection() == null){
			logger.error("Database connection failed and updating " + currentDep.getDepartmentName() + " department cannot be done.");
		}
		
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;
		boolean updateResult = false;
		

		try {
			dbConnection = instance.getConnection();
			preparedStatement = dbConnection.prepareStatement(updateDepartmentSQL);

			preparedStatement.setString(1, currentDep.getDepartmentCode());
			preparedStatement.setInt(2,departmentId );

			// execute insert SQL stetement
			preparedStatement.executeUpdate();
			
			updateResult = true;

		} catch (SQLException e) {

			logger.error("SQL Exception occured while updating " + currentDep.getDepartmentName() + " department.");

		} finally {

			if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}
		}
		return updateResult;
	}

	/**
	 * @param currentDep
	 * @throws SQLException
	 */
	private void insertNewDepartment(IDepartment currentDep) throws SQLException {
		TDatabaseConn instance = TDatabaseConn.getInstance();
		
		if (instance == null | instance.getConnection() == null){
			logger.error("Database connection failed and inserting departmen cannot be done.");
		}
		
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;

		

		try {
			dbConnection = instance.getConnection();
			preparedStatement = dbConnection.prepareStatement(insertDepartmentSQL);

			preparedStatement.setInt(1, currentDep.getFacultyID());
			preparedStatement.setString(2, currentDep.getDepartmentCode());
			preparedStatement.setString(3, currentDep.getDepartmentName());
			preparedStatement.setString(4, currentDep.getDepartmentName());
			preparedStatement.setBoolean(5, true);

			// execute insert SQL stetement
			preparedStatement.executeUpdate();

			System.out.println("Record is inserted into DBUSER table!");

		} catch (SQLException e) {

			System.out.println(e.getMessage());

		} finally {

			if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}

		}
	}
	

}
