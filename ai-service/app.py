from flask import Flask
from services.sanitizer import sanitize_request
from config import limiter
from routes.describe import describe_bp
from routes.report import report_bp

app = Flask(__name__)

# Initialize rate limiter with the app
limiter.init_app(app)

# Register blueprints
app.register_blueprint(report_bp)
app.register_blueprint(describe_bp)

# Register middleware
app.before_request(sanitize_request)

#error handling codee for bad requests and server errors
@app.errorhandler(400)
def bad_request(e):
    return {"error": "Bad Request"}, 400


@app.errorhandler(500)
def server_error(e):
    return {"error": "Internal Server Error"}, 500

@app.errorhandler(429)
def rate_limit_handler(e):
    return {
        "error": "Rate limit exceeded",
        "message": str(e.description)
    }, 429

if __name__ == "__main__":
    app.run(debug=True)