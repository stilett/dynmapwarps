componentconstructors['warps'] = function(dynmap, configuration) {
	var markers = {};
    var round = -1;
	
	var link = $("<link>");
	link.attr({
		type: 'text/css',
		rel: 'stylesheet',
		href: 'css/warps.css'
	});
	$("head").append( link ); 
	
	$(dynmap).bind('worldupdating', function() {
        round++;
        if (round % 5) return;
        round = 0;

		$.getJSON('marks', function(data) {
			$.each(markers, function(key, value) {
				markers[key].outdated = true;
			});

			for(var i = 0; i < data.marks.length; i++) {
                var mark = data.marks[i];
                var marker = markers[mark.name];
                
                if (!marker) {
                    marker = markers[mark.name] = {};
                    marker.marker = createMarker(mark);
                    marker.data = mark;
                } else {
					var markerPosition = dynmap.map.getProjection().fromWorldToLatLng(mark.x, mark.y, mark.z);
					marker.marker.setPosition(markerPosition);
                    marker.data = mark;
                }
                marker.outdated = false;
                marker.marker.toggle(dynmap.world.name == marker.data.world);
            }


			// Remove markers that are outdated
			$.each(markers, function(key, value) {
				if (markers[key].outdated) {
					markers[key].marker.remove();
					delete markers[key];
				}
			});
		});
	});
	
	$(dynmap).bind('mapchanged', function() {
		for(var name in markers) {
			var marker = markers[name];
            var data = marker.data;
			var markerPosition = dynmap.map.getProjection().fromWorldToLatLng(data.x, data.y, data.z);
			marker.marker.setPosition(markerPosition);
			marker.marker.toggle(dynmap.world.name == data.world);
		}
	});
}


function createMarker(data) {
	var markerPosition = dynmap.map.getProjection().fromWorldToLatLng(data.x, data.y, data.z);
	return new CustomMarker(markerPosition, dynmap.map, function(div) {
		$(div)
			.addClass('Marker')
			.addClass('warpMarker')
            .css({backgroundImage: "url(" + data.icon + ")"})
			.append($('<span/>')
				.addClass('warpName')
				.text(data.name));
	});
}


