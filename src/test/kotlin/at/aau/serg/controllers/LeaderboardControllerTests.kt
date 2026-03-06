package at.aau.serg.controllers

import at.aau.serg.models.GameResult
import at.aau.serg.services.GameResultService
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import kotlin.test.Test
import kotlin.test.assertEquals
import org.mockito.Mockito.`when` as whenever // when is a reserved keyword in Kotlin

class LeaderboardControllerTests {

    private lateinit var mockedService: GameResultService
    private lateinit var controller: LeaderboardController

    @BeforeEach
    fun setup() {
        mockedService = mock<GameResultService>()
        controller = LeaderboardController(mockedService)
    }

    @Test
    fun test_getLeaderboard_correctScoreSorting() {
        val first = GameResult(1, "first", 20, 20.0)
        val second = GameResult(2, "second", 15, 10.0)
        val third = GameResult(3, "third", 10, 15.0)

        whenever(mockedService.getGameResults()).thenReturn(listOf(second, first, third))

        val res: List<GameResult> = controller.getLeaderboard()

        verify(mockedService).getGameResults()
        assertEquals(3, res.size)
        assertEquals(first, res[0])
        assertEquals(second, res[1])
        assertEquals(third, res[2])
    }

    @Test
    fun test_getLeaderboard_sameScore_CorrectIdSorting() {
        val first = GameResult(1, "first", 20, 20.0)
        val second = GameResult(2, "second", 20, 10.0)
        val third = GameResult(3, "third", 20, 15.0)

        whenever(mockedService.getGameResults()).thenReturn(listOf(second, first, third))

        val res: List<GameResult> = controller.getLeaderboard()

        verify(mockedService).getGameResults()
        assertEquals(3, res.size)
        assertEquals(first, res[2])
        assertEquals(second, res[0])
        assertEquals(third, res[1])
    }

    @Test
    fun test_getLeaderboardWithBadRank() {
        val player1 = GameResult(1, "player1", 20, 20.0)
        val player2 = GameResult(2, "player2", 20, 10.0)
        val player3 = GameResult(3, "player3", 20, 15.0)
        val player4 = GameResult(1, "player4", 20, 20.0)
        val player5 = GameResult(2, "player5", 20, 10.0)
        val player6 = GameResult(3, "player6", 20, 15.0)
        val player7 = GameResult(3, "player7", 20, 15.0)

        whenever(mockedService.getGameResults()).thenReturn(listOf(player1, player2, player3, player4, player5, player6, player7))

        assertThrows<ResponseStatusException> {
            controller.getLeaderboard(-1)
        }
        assertThrows<ResponseStatusException> {
            controller.getLeaderboard(10)
        }
    }

    @Test
    fun test_getLeaderboardWithByRank() {
        val player1 = GameResult(1, "player1", 20, 10.0)
        val player2 = GameResult(2, "player2", 20, 15.0)
        val player3 = GameResult(3, "player3", 20, 20.0)
        val player4 = GameResult(4, "player4", 15, 10.0)
        val player5 = GameResult(5, "player5", 15, 15.0)
        val player6 = GameResult(6, "player6", 15, 20.0)
        val player7 = GameResult(7, "player7", 10, 15.0)

        whenever(mockedService.getGameResults()).thenReturn(listOf(player1, player2, player3, player4, player5, player6, player7))

        val res: List<GameResult> = controller.getLeaderboard(6)
        assertEquals(4, res.size)
        assertEquals("player4", res[0].playerName)
        assertEquals("player7", res[3].playerName)
    }

    @Test
    fun test_getLeaderboardWithShortList() {
        val player1 = GameResult(1, "player1", 20, 20.0)
        val player2 = GameResult(2, "player2", 20, 10.0)
        val player3 = GameResult(3, "player3", 20, 15.0)

        whenever(mockedService.getGameResults()).thenReturn(listOf(player1, player2, player3))

        val res: List<GameResult> = controller.getLeaderboard(0)
        assertEquals(3, res.size)
        assertEquals("player2", res[0].playerName)
    }
}