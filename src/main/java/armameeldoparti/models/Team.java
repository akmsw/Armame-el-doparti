package armameeldoparti.models;

import armameeldoparti.models.enums.Position;
import armameeldoparti.utils.common.CommonFields;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Team class.
 *
 * @author Bonino, Francisco Ignacio.
 *
 * @version 0.0.1
 *
 * @since 3.0
 */
public class Team {

  // ---------- Private fields -----------------------------------------------------------------------------------------------------------------------

  private int teamNumber;

  private Map<Position, List<Player>> teamPlayers;

  // ---------- Constructor --------------------------------------------------------------------------------------------------------------------------

  /**
   * Builds a basic team with empty position sets.
   *
   * @param teamNumber Integer identification for the team.
   */
  public Team(int teamNumber) {
    setTeamNumber(teamNumber);
    setTeamPlayers(new EnumMap<>(Position.class));

    for (Position position : Position.values()) {
      teamPlayers.put(position, new ArrayList<>());
    }
  }

  // ---------- Public methods -----------------------------------------------------------------------------------------------------------------------

  /**
   * Clears all players sets in the team.
   */
  public void clear() {
    teamPlayers.values()
               .stream()
               .flatMap(List::stream)
               .forEach(player -> player.setTeamNumber(0));

    teamPlayers.values()
               .forEach(List::clear);
  }

  /**
   * @return The number of players in the team.
   */
  public int getPlayersCount() {
    return teamPlayers.values()
                      .stream()
                      .mapToInt(List::size)
                      .sum();
  }

  /**
   * @return The number of players per position in the team.
   */
  public Map<Position, Integer> getPlayersCountPerPosition() {
    return teamPlayers.values()
                      .stream()
                      .flatMap(List::stream)
                      .collect(Collectors.toMap(Player::getPosition, _ -> 1, Integer::sum, () -> new EnumMap<>(Position.class)));
  }

  /**
   * @return The team skill points accumulated so far.
   */
  public int getTeamSkill() {
    return teamPlayers.values()
                      .stream()
                      .flatMap(List::stream)
                      .mapToInt(Player::getSkillPoints)
                      .sum();
  }

  /**
   * @param position The position of the set to check.
   *
   * @return Whether the specified position set in the team is full.
   */
  public boolean isPositionFull(Position position) {
    return teamPlayers.get(position)
                      .size() == CommonFields.getPlayersLimitPerPosition()
                                             .get(position);
  }

  // ---------- Getters ------------------------------------------------------------------------------------------------------------------------------

  public int getTeamNumber() {
    return teamNumber;
  }

  public Map<Position, List<Player>> getTeamPlayers() {
    return teamPlayers;
  }

  // ---------- Setters ------------------------------------------------------------------------------------------------------------------------------

  public void setTeamNumber(int teamNumber) {
    this.teamNumber = teamNumber;
  }

  public void setTeamPlayers(Map<Position, List<Player>> teamPlayers) {
    this.teamPlayers = teamPlayers;
  }
}