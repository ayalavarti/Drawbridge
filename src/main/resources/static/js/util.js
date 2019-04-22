function generateTrip(trip) {
    let date = new Date(parseFloat(trip["date"]) * 1000);
    // If the status is "join" add hover effects and an onclick handler
    let imgAtt = "";
    if (trip["status"] === "join") {
        imgAtt
            = `onmouseover="hover(this);" onmouseout="unhover(this);" onclick="handleJoin(${trip["id"]});"`;
    }
    let imgElement = `<div><img src="../images/${trip["status"]}-btn.png" class="${trip["status"]}-btn" ${imgAtt}/></div>`;

    return (`<a href='/trip/${trip["id"]}'><div class="result-info">
				<div class="sub-heading">${trip["name"]}</div>
				<img src="/images/divider.png" style="height: 3px; width: auto;" />
				<div>
					<i class="fas fa-map-marker-alt icon-label-small"></i>
						${trip["start"]} - ${trip["end"]}
				</div>
				<div>
					<i class="fas fa-calendar icon-label-small"></i>
						${date.toDateString()}
					<i class="fas fa-clock icon-label-small"></i>
						${toJSTime(date)}
				</div>
				<div>
					<i class="fas fa-users icon-label-small"></i>
						${trip["currentSize"]} / ${trip["maxSize"]}
						<i class="fas fa-dollar-sign icon-label-small"></i>
						${parseFloat(trip["costPerPerson"]).toFixed(2)}
				</div>
				${imgElement}
			</div></a>`)
}

function showHideTooltip(tooltip, timeout) {
    tooltip.show();
    setTimeout(function () {
        tooltip.hide();
    }, timeout);
}

function toJSTime(date) {
    let hour = date.getHours();
    let min = date.getMinutes();
    let output = `${((hour > 12) ? hour - 12 : hour)}`;
    if (hour === 0) {
        output = '12';
    }
    output += `${((min < 10) ? ':0' : ':') + min}`;
    output += (hour >= 12) ? ' PM' : ' AM';
    return output;
}
