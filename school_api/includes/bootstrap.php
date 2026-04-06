<?php
declare(strict_types=1);

header('Content-Type: application/json; charset=utf-8');

session_start([
    'cookie_samesite' => 'Lax',
    'cookie_httponly' => true,
]);

require_once __DIR__ . '/../config/database.php';

/**
 * @param array<string, mixed> $payload
 */
function json_response(int $code, array $payload): never {
    http_response_code($code);
    echo json_encode($payload, JSON_UNESCAPED_UNICODE);
    exit;
}

function require_login(): void {
    if (empty($_SESSION['user_id'])) {
        json_response(401, ['status' => 'error', 'message' => 'Not authenticated']);
    }
}

/** @return array<string, mixed> */
function read_json_body(): array {
    $raw = file_get_contents('php://input');
    $data = json_decode($raw ?: '{}', true);
    return is_array($data) ? $data : [];
}
