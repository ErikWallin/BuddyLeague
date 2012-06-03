
$(document).ready(function() {
	
	getLeagues();
	
	$('#leagueList a').live('click', function() {
		var id = $(this).data('identity');
		getLeague(id);
		getLeagueStandings(id);
	});
});

var rootURL = 'http://localhost:8080/rest/league';

function getLeagues() {
	$.ajax({
		type: 'GET',
		url: rootURL,
		dataType: "json",
		success: renderLeagues
	});
}

function getLeague(id) {
	$.ajax({
		type: 'GET',
		url: rootURL + '/' + id,
		dataType: "json",
		success: renderLeague
	});
}

function getLeagueStandings(id) {
	$.ajax({
		type: 'GET',
		url: rootURL + '/' + id + '/table',
		dataType: "json",
		success: renderLeagueStandings
	});
}

function renderLeagues(leagues) {
	$('#leagueList li').remove();
	$.each(leagues, function(index, league) {
		$('#leagueList').append('<li><a href="#" data-identity="' + league.id + '">' + league.id + '</a></li>');
	});
}

function renderLeague(league) {
	$('#leagueId').text(league.id);
	
	$('#playerList li').remove();
	$.each(league.players, function(index, player) {
		$('#playerList').append('<li><a href="#" data-identity="' + player.id + '">' + player.id + ': ' + player.firstName + ' ' + player.surName + '</a></li>');
	});
	
	$('#gameList li').remove();
	$.each(league.games, function(index, game) {
		$('#gameList').append('<li>[' + game.home + '] vs [' + game.away + '] ' + game.score.home + '-' + game.score.away + '</li>');
	});
}

function renderLeagueStandings(standings) {
	$('#standingsList li').remove();
	$.each(standings, function(index, entry) {
		$('#standingsList').append('<li>' + entry[0].firstName + ' ' + entry[0].surName + ' ' + entry[1].home + '-' + entry[1].away + '</li>');
	});
}