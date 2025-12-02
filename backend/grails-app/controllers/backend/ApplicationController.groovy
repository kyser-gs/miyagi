package backend

import org.springframework.core.io.Resource
import org.springframework.core.io.ResourceLoader
import org.springframework.beans.factory.annotation.Autowired

class ApplicationController {

    @Autowired
    ResourceLoader resourceLoader

    def index() {
        // Get the request URI
        String uri = request.forwardURI ?: request.requestURI

        // If the request is for a static file (has extension), try to serve it
        if (uri?.contains('.')) {
            String filename = uri.substring(uri.lastIndexOf('/') + 1)
            Resource staticResource = resourceLoader.getResource("classpath:public/${filename}")

            if (staticResource.exists()) {
                // Determine content type based on extension
                String contentType = getContentType(filename)
                response.contentType = contentType
                response.outputStream << staticResource.inputStream.bytes
                response.outputStream.flush()
                return null
            }
        }

        // Otherwise, serve Angular's index.html for SPA routing
        Resource resource = resourceLoader.getResource("classpath:public/index.html")

        if (resource.exists()) {
            response.contentType = "text/html;charset=UTF-8"
            response.outputStream << resource.inputStream.bytes
            response.outputStream.flush()
            return null
        } else {
            response.status = 404
            render(text: "Angular app not found. Run: cd ../frontend && npm run build")
        }
    }

    private String getContentType(String filename) {
        String extension = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase()
        switch (extension) {
            case 'js': return 'application/javascript'
            case 'css': return 'text/css'
            case 'html': return 'text/html'
            case 'json': return 'application/json'
            case 'ico': return 'image/x-icon'
            case 'png': return 'image/png'
            case 'jpg':
            case 'jpeg': return 'image/jpeg'
            case 'svg': return 'image/svg+xml'
            case 'txt': return 'text/plain'
            default: return 'application/octet-stream'
        }
    }
}