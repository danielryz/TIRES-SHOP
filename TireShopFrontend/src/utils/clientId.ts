export function getOrCreateClientId() {
  const KEY = "clientId";
  let clientId = localStorage.getItem(KEY);
  if (!clientId) {
    clientId = crypto.randomUUID();
    localStorage.setItem(KEY, clientId);
  }
  return clientId;
}
