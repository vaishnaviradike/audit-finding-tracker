from flask import Blueprint, g, jsonify
from flask_limiter import Limiter
from flask_limiter.util import get_remote_address

report_bp = Blueprint("report", __name__)

# Apply stricter limit here
from app import limiter

@report_bp.route("/generate-report", methods=["POST"])
@limiter.limit("10 per minute")
def generate_report():
    data = g.sanitized_data

    return jsonify({
        "message": "Report generated",
        "data": data
    })