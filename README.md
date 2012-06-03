BuddyLeague
===========

This project aims to provide individual statistics to a team activity. The
scenario is that you play floor ball with your friends once a week. It's often
many smaller games with different combinations of players. Some player maybe
win 4 of 5 games, and is thus ranked before a player with only 2 wins. This
project is mainly the REST Api, but provides a very simple web gui.

You can use the [REST Client for 
Firefox plugin](https://addons.mozilla.org/en-US/firefox/addon/9780/) to test
the methods.

REST URIs
--------

The listed REST URIs return all JSON responses or error pages. Note that during
development, this list might not be accurate. See 
[BuddyLeagueService.scala](https://github.com/ErikWallin/BuddyLeague/blob/master/src/main/scala/se/marfok/buddyleague/rest/BuddyLeagueService.scala)
for the most correct information

Since this is not in production, the base URI is not set. In development, it's
localhost:8080/rest

### Get leagues
GET http://{base uri}/league

### Create league
POST http://{base uri}/league

### Get league
GET http://{base uri}/league/{leagueId}

### Delete league
DELETE http://{base uri}/league/{leagueId}

### Get league table (standings)
DELETE http://{base uri}/league/{leagueId}/table

### Add player to league
POST http://{base uri}/league/{leagueId}/player

### Delete player to league
DELETE http://{base uri}/league/{leagueId}/player/{playerId}

### Add game to league
POST http://{base uri}/league/{leagueId}/game

### Delete game to league
DELETE http://{base uri}/league/{leagueId}/game/{gameId}
