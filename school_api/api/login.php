<?php
declare(strict_types=1);

require_once __DIR__ . '/../includes/bootstrap.php';

if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    json_response(405, ['status' => 'error', 'message' => 'Method not allowed']);
}

$body = read_json_body();
$username = trim((string) ($body['username'] ?? ''));
$password = (string) ($body['password'] ?? '');
$role = trim((string) ($body['role'] ?? ''));

if ($username === '' || $password === '' || $role === '') {
    json_response(400, ['status' => 'error', 'message' => 'username, password, and role are required']);
}

$stmt = $pdo->prepare('SELECT id, username, password, role FROM users WHERE username = ? AND role = ? LIMIT 1');
$stmt->execute([$username, $role]);
$user = $stmt->fetch();

if (!$user) {
    json_response(401, ['status' => 'error', 'message' => 'Invalid username or role']);
}

$hash = $user['password'];
$ok = (strlen($hash) === 32 && ctype_xdigit($hash))
    ? (md5($password) === strtolower($hash))
    : password_verify($password, $hash);

if (!$ok) {
    json_response(401, ['status' => 'error', 'message' => 'Invalid password']);
}

$_SESSION['user_id'] = (int) $user['id'];
$_SESSION['username'] = $user['username'];
$_SESSION['role'] = $user['role'];

json_response(200, [
    'status' => 'success',
    'message' => 'Login successful',
    'user_id' => (string) $user['id'],
    'username' => $user['username'],
    'role' => $user['role'],
]);
