package office.effective.workspace

import junit.framework.TestCase.assertEquals
import office.effective.common.exception.InstanceNotFoundException
import office.effective.common.exception.ValidationException
import office.effective.common.utils.DatabaseTransactionManager
import office.effective.features.workspace.converters.WorkspaceFacadeConverter
import office.effective.features.workspace.dto.WorkspaceDTO
import office.effective.features.workspace.facade.WorkspaceFacade
import office.effective.features.workspace.service.WorkspaceService
import office.effective.model.Workspace
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.ktorm.database.TransactionIsolation
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.invocation.InvocationOnMock
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.eq
import org.mockito.kotlin.whenever
import java.util.*
import kotlin.test.assertFailsWith


class WorkspaceFacadeTest {
    @Mock
    private lateinit var mockService: WorkspaceService
    @Mock
    private lateinit var mockConverter: WorkspaceFacadeConverter
    @Mock
    private lateinit var mockTransactionManager: DatabaseTransactionManager

    private lateinit var workspaceFacade: WorkspaceFacade

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        workspaceFacade = WorkspaceFacade(mockService, mockConverter, mockTransactionManager)
    }

    private fun setUpMockService(workspace: Workspace?) {
        whenever(mockService.findById(anyOrNull())).thenReturn(workspace)
    }

    private fun setUpMockConverter(workspace: Workspace, workspaceDTO: WorkspaceDTO) {
        whenever(mockConverter.workspaceModelToDto(workspace)).thenReturn(workspaceDTO)
    }

    private fun setUpMockTransactionManager() {
        whenever(mockTransactionManager.useTransaction<Workspace?>(
            anyOrNull(),
            eq(TransactionIsolation.READ_COMMITTED))
        ).thenAnswer { invocation: InvocationOnMock ->
            @Suppress("UNCHECKED_CAST")
            val lambda = invocation.arguments[0] as () -> Workspace?
            lambda()
        }
    }

    @Test
    fun testFindById() {
        val workspaceId = UUID.randomUUID()
        val existingWorkspace = Workspace(workspaceId, "Workspace 1", "Tag", emptyList())
        val expectedWorkspaceDTO = WorkspaceDTO(workspaceId.toString(), "Workspace 1", emptyList())

        setUpMockTransactionManager()
        setUpMockService(existingWorkspace)
        setUpMockConverter(existingWorkspace, expectedWorkspaceDTO)

        val result = workspaceFacade.findById(workspaceId.toString())

        assertEquals(expectedWorkspaceDTO, result)
    }

    @Test
    fun testFindByIdWithNonExistingWorkspace() {
        val workspaceId = UUID.randomUUID()

        setUpMockTransactionManager()
        setUpMockService(null)

        assertFailsWith<InstanceNotFoundException> {
            workspaceFacade.findById(workspaceId.toString())
        }
    }

    @Test
    fun testFindByIdWithInvalidUuidInput() {
        val invalidWorkspaceId = "not_a_valid_uuid"

        assertFailsWith<ValidationException> {
            workspaceFacade.findById(invalidWorkspaceId)
        }
    }

    @Test
    fun testFindAllByTag() {
        val workspace1Id = UUID.randomUUID()
        val workspace2Id = UUID.randomUUID()
        val existingList = listOf(
            Workspace(workspace1Id, "Workspace 1", "Tag", emptyList()),
            Workspace(workspace2Id, "Workspace 2", "Tag", emptyList())
        )
        val expectedList = listOf(
            WorkspaceDTO(workspace1Id.toString(), "Workspace 1", emptyList()),
            WorkspaceDTO(workspace2Id.toString(), "Workspace 2", emptyList())
        )

        setUpMockTransactionManager()
        whenever(mockService.findAllByTag(anyOrNull())).thenReturn(existingList)
        whenever(mockConverter.workspaceModelToDto(anyOrNull())).thenReturn(expectedList[0], expectedList[1])

        val result = workspaceFacade.findAllByTag("tag")

        assertEquals(expectedList, result)
    }
}
