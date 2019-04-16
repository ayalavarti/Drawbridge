<#assign stylesheets>
    <link rel="stylesheet" href="/css/create.css" type="text/css">
</#assign>

<#assign content>
    <div id="container">
        <div class="search-menu">
            <div>
                <h2 class="section-heading">Host a Carpool</h2>
                <img alt="divider" src="/images/divider.png" style="height: 4px;
                width: auto;"/>
            </div>

            <form autocomplete="off"
                  onsubmit="event.preventDefault(); handleSubmit()">
                <div class="search-inputs">
                    <div>
                        <i class="fas fa-dot-circle icon-label"></i>
                        <input class="address-input" id="start-input"
                               onblur="handleInput('start-input', 0)"
                               type="text" placeholder="Starting point..."/>
                        <img alt="loading" id="loading-start-input"
                             src="/images/loading.gif"
                             class="loading-address-gif"/>
                        <i class="fas fa-map-marker-alt icon-label"></i>
                        <input class="address-input" id="end-input"
                               onblur="handleInput('end-input', 1)" type="text"
                               placeholder="Ending destination..."/>
                        <img alt="loading" id="loading-end-input"
                             src="/images/loading.gif"
                             class="loading-address-gif"/>
                    </div>

                    <div>
                        <i class="fas fa-calendar icon-label"></i>
                        <input class="datetime-input flatpickr flatpickr-input"
                               id="date" type="text" placeholder="Date..."/>

                        <i class="fas fa-clock icon-label"></i>
                        <input class="datetime-input flatpickr flatpickr-input"
                               id="time" type="text" placeholder="Time..."/>
                    </div>

                    <div class="input-info">
                        <div>
                            <span class="sub-heading">Carpool Size</span>
                            <div>
                                <i class="fas fa-users icon-label"></i>
                                <input data-tippy-content="Minimum carpool size must be 1."
                                       class="form-tooltip size-input"
                                       id="carpool-size" type="number"
                                       placeholder="Size..."/>
                            </div>
                        </div>
                        <div>
                            <span class='sub-heading'>Transport Type</span>
                            <div>
                                <i class="fas fa-car icon-label"></i>
                                <select class="transport-type"
                                        id="transport-type"
                                        name="transport-type">
                                    <option value="" disabled selected hidden>
                                        Select
                                        your option
                                    </option>
                                    <option value="personal">Personal
                                        Car
                                    </option>
                                    <option value="uber">Uber</option>
                                    <option value="lyft">Lyft</option>
                                    <option value="other">Other</option>
                                </select>
                            </div>
                        </div>
                        <div>
                            <span class="sub-heading">Expected Price</span>
                            <div>
                                <i class="fas fa-dollar-sign icon-label"></i>
                                <input data-tippy-content="Minimum price must be $0."
                                       class="form-tooltip price-input"
                                       id="expected-price" type="number"
                                       step="0.01" placeholder="Price..."/>
                                <span style="font-size: 12px;"><i
                                            class="addendum">/person</i></span>
                            </div>
                        </div>
                        <div>
                            <span class="sub-heading">Contact Number</span>
                            <div>
                                <i class="fas fa-phone icon-label"></i>
                                <input data-tippy-content="Required phone format is 123-456-7890."
                                       class="form-tooltip phone-input"
                                       id="contact-number" type="tel"
                                       placeholder="Phone..."/>
                            </div>
                        </div>
                    </div>

                    <div class="input-info">
                        <div>
                            <span class="sub-heading">Comments</span>
                            <div>
                                <i class="fas fa-comments icon-label"></i>
                                <textarea class="comments-input" id="comments"
                                          type="text"
                                          placeholder="Comments..."></textarea>
                            </div>
                        </div>
                    </div>

                    <input id="requiredTooltip" name="submit" alt="Submit"
                           type="image" src="/images/submit-btn.png"
                           class="submit-btn"
                           data-tippy-content="Please enter all inputs."
                           onmouseover="hover(this);"
                           onmouseout="unhover(this);"/>
                </div>
            </form>
        </div>
    </div>
    <script type="text/javascript">
        mapboxgl.accessToken = "${mapboxKey?js_string}";
    </script>
    <script src="/js/mapUtil.js"></script>
    <script src="/js/util.js"></script>
    <script src="/js/create.js"></script>
</#assign>
<#include "main.ftl">
