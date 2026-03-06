package at.aau.serg.controllers

import at.aau.serg.models.GameResult
import at.aau.serg.services.GameResultService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/leaderboard")
class LeaderboardController(
    private val gameResultService: GameResultService
) {

    @GetMapping
    fun getLeaderboard(): List<GameResult> =
        gameResultService.getGameResults().sortedWith(compareBy({ -it.score }, { +it.timeInSeconds}))


    @GetMapping
    fun getLeaderboard(rank: Int): List<GameResult> { //Rank is index of list (Starting with 0)
        var leaderboard = getLeaderboard()

        if (rank < 0 || rank >= leaderboard.size) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Rank out of bounds")
        }

        val startIndex = maxOf(0, rank - 3)
        val endIndex = minOf(leaderboard.size - 1, rank + 3)

        return leaderboard.slice(startIndex..endIndex)
    }
}