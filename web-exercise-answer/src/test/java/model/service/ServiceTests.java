package model.service;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;


@Suite
@SuiteDisplayName("Serviceクラスの単体テストスイート")
@SelectClasses({ 
	DeleteDepartmentServiceTest.class, 
	DeleteEmployeeServiceTest.class,
	GetDepartmentListServiceTest.class,
	GetEmployeeListServiceTest.class,
	InsertDepartmentServiceTest.class,
	InsertEmployeeServiceTest.class,
	UpdateDepartmentServiceTest.class,
	UpdateEmployeeServiceTest.class
	})
public class ServiceTests {

}
