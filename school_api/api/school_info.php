<?php
declare(strict_types=1);

require_once __DIR__ . '/../includes/bootstrap.php';
require_login();

if ($_SERVER['REQUEST_METHOD'] !== 'GET') {
    json_response(405, ['status' => 'error', 'message' => 'Method not allowed']);
}

$stmt = $pdo->query('SELECT school_name, address, phone, email, principal FROM school_info ORDER BY id ASC LIMIT 1');
$row = $stmt->fetch();

if (!$row) {
    json_response(404, ['status' => 'error', 'message' => 'No school info']);
}

echo json_encode($row, JSON_UNESCAPED_UNICODE);
