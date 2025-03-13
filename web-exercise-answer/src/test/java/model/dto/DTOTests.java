package model.dto;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("DTOクラスの単体テストスイート")
@SelectClasses({ DepartmentTest.class, EmployeeTest.class })
public class DTOTests {

}
