NCKU Program Design 2 2024 Spring Final Project
===============================================
Project Name : Discord weather bot
=============================================== 
Target : <br/><br/>
Using Jsoup to get weather info from<br/>
weather.com and using JDA to create<br/>
a discord bot.<br/><br/>
=============================================== 
Current fucntion :<br/><br/>
get current weather,<br/>
get detail weather,<br/>
get hourly forecast,<br/>
get daily forecast,<br/>
get weather info around given airport.<br/><br/>
=============================================== 
How to use :<br/><br/>default , <br/>
             weather [location] [options]<br/><br/>
             airport ,<br/>
             travel [airport code] [options]<br/><br/>
             add city ,<br/>
             /addcity [city] [url]<br/><br/>
             add airport ,<br/>
             /addtraveldest [airport] [url]<br/><br/>
             [url] must from weather.com
=============================================== 
Because we store locations url in json file,<br/>
so currently,<br/><br/>
[location] : <br/>
Counties in Taiwan are available.<br/><br/>
[airport code] : <br/>
airports that Taiwanese travel with the most.<br/><br/>
[options] : <br/> <br/>
null : get current weather in given location.<br/>
detail : get detail weather in given location.<br/>
hourly : get hourly weather in given location.<br/>
daily : get daily weather in given location.<br/><br/>
=============================================== 
