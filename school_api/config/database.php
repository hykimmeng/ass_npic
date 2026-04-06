<?php
/**
 * XAMPP MySQL on port 3307 (default user root, empty password).
 * Change $dbPassword if you set a MySQL password.
 */
declare(strict_types=1);

$dbHost = '127.0.0.1';
$dbPort = '3307';
$dbName = 'nipc_school';
$dbUser = 'root';
$dbPass = '';

$dsn = "mysql:host={$dbHost};port={$dbPort};dbname={$dbName};charset=utf8mb4";

try {
    $pdo = new PDO($dsn, $dbUser, $dbPass, [
        PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION,
        PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
    ]);
} catch (PDOException $e) {
    http_response_code(500);
    header('Content-Type: application/json; charset=utf-8');
    echo json_encode([
        'status' => 'error',
        'message' => 'Database connection failed. Import database/nipc_school.sql and check MySQL port 3307.',
    ], JSON_UNESCAPED_UNICODE);
    exit;
}
