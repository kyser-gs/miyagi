package backend

class UrlMappings {

    static mappings = {
        "/"(controller: 'application', action: 'index')
        "/api/$controller/$action?/$id?"()  // REST API endpoints

        "500"(view:'/error')
        "404"(controller: 'application', action: 'index')

        // SPA fallback for client-side routing (but not for files with extensions)
        "/**"(controller: 'application', action: 'index')
    }
}