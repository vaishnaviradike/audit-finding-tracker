from flask import Flask
from services.sanitizer import sanitize_request
from routes.describe import describe_bp
from flask_limiter import Limiter
from flask_limiter.util import get_remote_address
from routes.report import report_bp

app = Flask(__name__)

# Rate limiting configuration
limiter = Limiter(
    key_func=get_remote_address,   # identify users by IP
    default_limits=["30 per minute"]  # global limit
)
limiter.init_app(app)

app.register_blueprint(report_bp)

# Register middleware
app.before_request(sanitize_request)

#register routes
app.register_blueprint(describe_bp)

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