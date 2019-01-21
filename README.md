# Video walkthroughs (click to view video):

## Introduction / benefits / comparison:

<a href="https://www.youtube.com/watch?v=hPwjEDE1Oks" target="_blank"><img src="https://img.youtube.com/vi/hPwjEDE1Oks/0.jpg" alt="Introduction Video"/></a>

## Developer walkthrough - basic

<a href="IRQacpaZ6GY" target="_blank"><img src="https://img.youtube.com/vi/IRQacpaZ6GY/0.jpg" alt="Basic Developer walkthrough"/></a>

## Developer walkthrough - advanced

<a href="https://www.youtube.com/watch?v=_F2qyZiXnZo" target="_blank"><img src="https://img.youtube.com/vi/_F2qyZiXnZo/0.jpg" alt="Advanced developer walkthrough"/></a>

## Author + dispatcher demo

<a href="https://www.youtube.com/watch?v=H41ZFSEW0DM" target="_blank"><img src="https://img.youtube.com/vi/H41ZFSEW0DM/0.jpg" alt="Author and Dispatcher demo"/></a>

# wcm-layout-pages
Render header and footer based on content pages dedicated for this purpose only.
Pages have it's own special template and are included with the content rendition.

## Benefits 

### User friendly to author
Alot of properties for each menu entry? No problem. Go as far as 300+ properties if you want. These are located in the page dialog of menu entries and thus easy to author.
Using the templating system from WCM pages in AEM, authors can only create the correct entries per level.

### Leverage MSM
Multi site manager can be leveraged to manage the page headers / footers without too much customization.
This makes the solution extremely scale-able. 
### Flexibility for authors
Layout items such as menu entries are not tied to the existing content as these are decoupled.
You can reference any header / footer from anywhere.

### Leverage the template designer
With the newly released template designer, and a parsys in the header / footer, you can create specific header and footer templates for your authors to use, allowing you to narrow down what can be done in a header or footer without deploying code.


### Flexibility for developers
Developers can easily create a new menu entry without affecting existing code. Just create a new menu entry template with its own render logic.
This makes the solution also very scale-able code-wise. The solution is very plug-gable and can be easily extended to fit new requirements.

### Supports personalization
Need a personalized header? An example is integrated that serves different header entries based on a cookie value.
This can also be a header value, session value or anything else available to the SlingHttpRequest. 
This way you can serve different cached headers for different user types. 
Note: For this to work on the dispatcher / CDN, [SDI (Sling Dynamic Include)](https://sling.apache.org/documentation/bundles/dynamic-includes.html) needs to be installed or an SSI/ ESI / AJAX statements need to be manually written. 


### Supports extensive caching
For the best performance even for personalized items, and example integration is provided with [ACS commons - HTTP cache](https://adobe-consulting-services.github.io/acs-aem-commons/features/http-cache/index.html).
The header is cached by the http cache using osgi configurations for the most part.
Only some code is required to create the keys (keyfactory).
Warning: This does presume that the header / footer is resource based, not request based, and that every locale has its own header / footer pages.
If you have request based elements such as a welcome message showing the user name, these will need to be resolved with either SSI or javascript. The project demonstrates this using Sling Dynamic Include and SSI.
Alternatively you can use the transformer pipeline as well to resolve placeholders, as this pipeline is applied after the filter chain if that is required.


## Drawbacks
The only real drawback there is, is that there is a big initial complexity.
Once established however, this solution is perfect to distribute complexity in a big enterprise environment, due to the composition.
The learning curve is still steep, however.
For smaller environments with little complexity this might not be a good solution.

Also, caching and stale content is something to be mindful of with this solution.

However by providing a good starting example, this should help you out to mitigate this.

### AEM compatibility
The package is designed for AEM 6.3 and later.
Without the extensions this should work on pretty much all AEM environments, but for AEM commons / SDI to work, 6.3 is recommended.
The package serves as example and / or starting point and not as OOTB solution, so customization will always be needed.

