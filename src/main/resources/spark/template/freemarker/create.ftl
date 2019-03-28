<#assign stylesheets>
    <link rel="stylesheet" href="/css/create.css"  type="text/css">
</#assign>

<#assign content>
    <div id="container">
        <div class="search-menu">
            <h2 class="section-heading">Host a Carpool</h2>
            <img src="/images/divider.png" style="height: 4px; width: auto;" />
            <form autocomplete="off" onsubmit="event.preventDefault(); handleSubmit()">
                <div class="search-inputs">
                    <div><i class="fas fa-dot-circle icon-label"></i>
                        <input class="address-input" id="start-input" onblur="handleInput('start-input', 0)" type="text" placeholder="Starting point..." />
                        <img id="loading-start-input" src ="/images/loading.gif" class="loading-address-gif"/>
                    </div>
                    <div><i class="fas fa-map-marker-alt icon-label"></i>
                        <input class="address-input" id="end-input" onblur="handleInput('end-input', 1)" type="text" placeholder="Ending destination..." />
                        <img id="loading-end-input" src ="/images/loading.gif" class="loading-address-gif"/>
                    </div>

                    <div>
                        <i class="fas fa-calendar icon-label"></i>
                        <input class="datetime-input flatpickr flatpickr-input" id="date" type="text" placeholder="Date..." />

                        <i class="fas fa-clock icon-label"></i>
                        <input class="datetime-input flatpickr flatpickr-input" id="time" type="text" placeholder="Time..." />
                    </div>

                    <div class="input-info">
                        <div>
                            <span class="sub-heading">Carpool Size</span>
                            <div>
                                <i class="fas fa-users icon-label"></i>
                                <input class="size-input" id="carpool-size" min="1" max="30" type="number" placeholder="Size..." />
                            </div>
                        </div>
                        <div>
                            <span class="sub-heading">Expected Price</span>
                            <div>
                                <i class="fas fa-dollar-sign icon-label-large"></i>
                                <input class="price-input" id="expected-price" min="0" type="number" placeholder="Price..." />
                                    <span style="font-size: 12px;">/person</span>
                            </div>
                        </div>
                        <div>
                            <span class="sub-heading">Contact Number</span>
                            <div>
                                <i class="fas fa-phone icon-label-large"></i>
                                <input class="phone-input" id="contact-number" type="tel" placeholder="Phone..." pattern="[0-9]{3}-[0-9]{3}-[0-9]{4}"/>
                            </div>
                        </div>
                    </div>

                    <div class="input-info">
                        <div>
                            <span class="sub-heading">Comments</span>
                            <div>
                                <i class="fas fa-comments icon-label"></i>
                                <input class="comments-input" id="comments" type="text" placeholder="Comments..." />
                            </div>
                        </div>
                    </div>

                    <input id="requiredTooltip" name="submit" alt="Submit" type="image" src="/images/submit-btn.png" class="submit-btn"
                            data-tippy-content="Please enter all inputs." onmouseover="hover(this);" onmouseout="unhover(this);"/>
                </div>
            </form>
        </div>
    </div>
    <script src="/js/create.js"></script>
</#assign>
<#include "main.ftl">