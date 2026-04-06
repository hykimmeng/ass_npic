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

if ($username === '' || $password === '' || !in_array($role, ['admin', 'user'], true)) {
    json_response(400, ['status' => 'error', 'message' => 'Invalid registration data']);
}

$check = $pdo->prepare('SELECT id FROM users WHERE username = ?');
$check->execute([$username]);
if ($check->fetch()) {
    json_response(409, ['status' => 'error', 'message' => 'Username already exists']);
}

$hash = md5($password);
$ins = $pdo->prepare('INSERT INTO users (username, password, role) VALUES (?, ?, ?)');
$ins->execute([$username, $hash, $role]);

json_response(200, [
    'status' => 'success',
    'message' => 'Account created',
    'user_id' => (string) $pdo->lastInsertId(),
    'username' => $username,
    'role' => $role,
]);
