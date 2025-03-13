
package model.dao;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("DAOクラスの単体テストスイート")
@SelectClasses({ DepartmentDAOTest.class, EmployeeDAOTest.class })
public class DAOTests {

}
