
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
		$('#playerList').append('<li>' + player.firstName + ' ' + player.surName + '</li>');
	});
	
	$('#gameList li').remove();
	$.each(league.games, function(index, game) {
		var item = '<li>[';
		$.each(game.home, function(index, id) {
			var homePlayer = league.players[id - 1];
			item += homePlayer.firstName + ' ' + homePlayer.surName + ', ';
		});
		item = item.substr(0, item.length - 2) + '] vs [';
		$.each(game.away, function(index, id) {
			var awayPlayer = league.players[id - 1];
			item += awayPlayer.firstName + ' ' + awayPlayer.surName + ', ';
		});
		item = item.substr(0, item.length - 2) + ']: ';
		item += game.score.home + '-' + game.score.away + '</li>';
		$('#gameList').append(item);
	});
}

function renderLeagueStandings(standings) {
	$('#standingsTable tr').remove();
	$.each(standings, function(index, entry) {
		$('#standingsTable').append('<tr><td>' + (index + 1) + '</td><td>' + entry[0].firstName + ' ' + entry[0].surName + '</td><td class=\"score\">' + entry[1].home + '</td><td class=\"score\">' + entry[1].away + '</td></tr>');
	});
}