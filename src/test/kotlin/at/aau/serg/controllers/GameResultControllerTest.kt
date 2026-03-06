package at.aau.serg.controllers

import at.aau.serg.models.GameResult
import at.aau.serg.services.GameResultService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test

class GameResultControllerTest {
    private lateinit var controller: GameResultController

    @BeforeEach
    fun setup() {
        controller = GameResultController(GameResultService())
    }

    @Test
    fun test_HappyPath() {
        controller.addGameResult(GameResult(1, "player1", 20, 10.0))
        controller.addGameResult(GameResult(2, "player2", 18, 10.0))
        controller.addGameResult(GameResult(3, "player3", 21, 10.0))

        assertEquals(3, controller.getAllGameResults().size)
        assertEquals("player3", controller.getGameResult(3)?.playerName)

        controller.deleteGameResult(2)
        assertEquals(2, controller.getAllGameResults().size)
    }

}