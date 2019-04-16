function generateTrip(trip) {
    let date = new Date(parseFloat(trip["date"]) * 1000);
    console.log(date);
    let hourLabel = "AM";
    let minutes;
    let hour = date.getHours();
    if (hour > 12) {
        hour = hour - 12;
        hourLabel = "PM";
    }
    if (date.getMinutes() < 10) {
        minutes = `${date.getMinutes()}0`;
    } else {
        minutes = date.getMinutes();
    }
    // If the status is "join" add hover effects and an onclick handler
    let imgAtt = "";
    if (trip["status"] === "join") {
        imgAtt
            = `onmouseover="hover(this);" onmouseout="unhover(this);" onclick="handleJoin(${trip["id"]});"`;
    }
    let imgElement
            = `<div><img src="../images/${trip["status"]}-btn.png" class="${trip["status"]}-btn" ${imgAtt}/></div>`;

    return (`<div class="result-info">
				<div class="sub-heading">${trip["name"]}</div>
				<img src="/images/divider.png" style="height: 3px; width: auto;" />
				<div>
					<i class="fas fa-map-marker-alt icon-label-small"></i>
						${trip["start"]} - ${trip["end"]}
				</div>
				<div>
					<i class="fas fa-calendar icon-label-small"></i>
						${months[date.getMonth()]} ${date.getDate()}, ${date.getFullYear()}
					<i class="fas fa-clock icon-label-small"></i>
						${hour}:${minutes} ${hourLabel}
				</div>
				<div>
					<i class="fas fa-users icon-label-small"></i>
						${trip["currentSize"]} / ${trip["maxSize"]}
						<i class="fas fa-dollar-sign icon-label-small"></i>
						${parseFloat(trip["costPerPerson"]).toFixed(2)}
				</div>
				${imgElement}
			</div>`)
}

function showHideTooltip(tooltip) {
    tooltip.show();
    setTimeout(function () {
        tooltip.hide();
    }, 3000);
}
