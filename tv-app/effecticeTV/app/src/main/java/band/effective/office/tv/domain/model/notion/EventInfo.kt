package band.effective.office.tv.domain.model.notion

sealed class EmployeeInfo(val name: String, val photoUrl: String, val eventType: EventType)

class Birthday(
    val employeeName: String,
    val employeePhotoUrl: String,
) : EmployeeInfo(employeeName, employeePhotoUrl, EventType.Birthday)

class Anniversary(
    private val employeeName: String,
    private val employeePhotoUrl: String,
    val yearsInCompany: Int
) : EmployeeInfo(employeeName, employeePhotoUrl, EventType.Anniversary)

class NewEmployee(
    private val employeeName: String,
    private val employeePhotoUrl: String,
) : EmployeeInfo(employeeName, employeePhotoUrl, EventType.NewEmployee)
