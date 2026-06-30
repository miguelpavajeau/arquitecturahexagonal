const API_BASE_URL = import.meta.env.PUBLIC_API_BASE_URL;
const TOKEN_KEY = "powerup_token";

/**
 * Wrapper de fetch que adjunta el token, serializa JSON y normaliza los errores
 * que devuelve el backend (campo `errors`, string o arreglo).
 */
async function request(path, { method = "GET", body, auth = true } = {}) {
  const headers = { "Content-Type": "application/json" };

  if (auth) {
    const token = getToken();
    if (token) headers.Authorization = `Bearer ${token}`;
  }

  const response = await fetch(`${API_BASE_URL}${path}`, {
    method,
    headers,
    body: body !== undefined ? JSON.stringify(body) : undefined,
  });

  const data = await response.json().catch(() => ({}));

  if (!response.ok) {
    if (response.status === 401) clearToken();
    const raw = data.errors ?? data.message;
    const message = Array.isArray(raw) ? raw.join(", ") : raw;
    throw new Error(message || `Error ${response.status}`);
  }

  return data;
}

// ----- Autenticación --------------------------------------------------------

export async function login(correo, clave) {
  const data = await request("/api/v1/auth/login", {
    method: "POST",
    body: { correo, clave },
    auth: false,
  });
  return data.token;
}

// ----- Usuarios -------------------------------------------------------------

export function createUser(payload) {
  return request("/api/v1/user", { method: "POST", body: payload });
}

export function getUsers() {
  return request("/api/v1/user");
}

/** Crea un empleado (solo PROPIETARIO autenticado). */
export function createEmployee(payload) {
  return request("/api/v1/user/employee", { method: "POST", body: payload });
}

/** Registro público de un cliente (no requiere autenticación). */
export function createClient(payload) {
  return request("/api/v1/user/client", { method: "POST", body: payload, auth: false });
}

// ----- Restaurantes ---------------------------------------------------------

export function createRestaurant(payload) {
  return request("/api/v1/restaurants", { method: "POST", body: payload });
}

export function getRestaurants() {
  return request("/api/v1/restaurants");
}

/** Lista paginada de restaurantes (nombre + logo) para el CLIENTE. */
export function listRestaurants(page = 0, size = 10) {
  return request(`/api/v1/restaurants/list?page=${page}&size=${size}`);
}

// ----- Categorías -----------------------------------------------------------

export function createCategory(payload) {
  return request("/api/v1/categories", { method: "POST", body: payload });
}

export function getCategories() {
  return request("/api/v1/categories");
}

// ----- Platos ---------------------------------------------------------------

export function createDish(payload) {
  return request("/api/v1/dishes", { method: "POST", body: payload });
}

export function updateDish(dishId, payload) {
  return request(`/api/v1/dishes/${dishId}`, { method: "PATCH", body: payload });
}

/** Habilita o deshabilita un plato (solo el propietario dueño del restaurante). */
export function changeDishStatus(dishId, activo) {
  return request(`/api/v1/dishes/${dishId}/status`, { method: "PATCH", body: { activo } });
}

/**
 * Lista paginada de platos activos de un restaurante (solo CLIENTE), opcionalmente
 * filtrada por categoría.
 */
export function listDishesByRestaurant(restaurantId, { categoryId, page = 0, size = 10 } = {}) {
  const params = new URLSearchParams({ page, size });
  if (categoryId) params.set("categoryId", categoryId);
  return request(`/api/v1/dishes/restaurant/${restaurantId}?${params.toString()}`);
}

// ----- Pedidos --------------------------------------------------------------

/** Crea un pedido (CLIENTE). payload = { idRestaurante, platos: [{idPlato, cantidad}] }. */
export function createOrder(payload) {
  return request("/api/v1/orders", { method: "POST", body: payload });
}

/** Lista paginada de pedidos del restaurante del empleado, filtrable por estado (EMPLEADO). */
export function listOrders({ estado, page = 0, size = 10 } = {}) {
  const params = new URLSearchParams({ page, size });
  if (estado) params.set("estado", estado);
  return request(`/api/v1/orders?${params.toString()}`);
}

/** El empleado se asigna el pedido y lo pasa a "en preparación". */
export function assignOrder(orderId) {
  return request(`/api/v1/orders/${orderId}/assign`, { method: "PATCH" });
}

/** Marca el pedido como listo y notifica al cliente por SMS con el PIN. */
export function markOrderReady(orderId) {
  return request(`/api/v1/orders/${orderId}/ready`, { method: "PATCH" });
}

/** Entrega el pedido validando el PIN del cliente. */
export function deliverOrder(orderId, pin) {
  return request(`/api/v1/orders/${orderId}/delivered`, { method: "PATCH", body: { pin } });
}

/** Cancela un pedido propio (CLIENTE, solo si está pendiente). */
export function cancelOrder(orderId) {
  return request(`/api/v1/orders/${orderId}/cancel`, { method: "PATCH" });
}

// ----- Sesión / token -------------------------------------------------------

export function decodeJwt(token) {
  const payload = token.split(".")[1];
  const decoded = atob(payload.replace(/-/g, "+").replace(/_/g, "/"));
  return JSON.parse(decoded);
}

export function saveToken(token) {
  localStorage.setItem(TOKEN_KEY, token);
}

export function getToken() {
  return localStorage.getItem(TOKEN_KEY);
}

export function clearToken() {
  localStorage.removeItem(TOKEN_KEY);
}

/**
 * Devuelve los datos de la sesión activa ({ correo, role, id }) o `null` si no
 * hay token o este ya expiró.
 */
export function getSession() {
  const token = getToken();
  if (!token) return null;
  try {
    const payload = decodeJwt(token);
    if (payload.exp && payload.exp * 1000 < Date.now()) {
      clearToken();
      return null;
    }
    return { correo: payload.sub, role: payload.role, id: payload.id };
  } catch {
    clearToken();
    return null;
  }
}

/** Redirige al login si no hay sesión, o si el rol no está permitido. */
export function requireRole(roles) {
  const session = getSession();
  if (!session) {
    window.location.href = "/";
    return null;
  }
  if (roles && roles.length && !roles.includes(session.role)) {
    window.location.href = "/dashboard";
    return null;
  }
  return session;
}
