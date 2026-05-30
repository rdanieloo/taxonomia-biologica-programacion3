function show(title, payload) {
  const el = document.getElementById("result");
  el.style.color = "";
  el.textContent = `${title}\n\n${JSON.stringify(payload, null, 2)}`;
}

function showError(message) {
  const el = document.getElementById("result");
  el.style.color = "#b02a37";
  el.textContent = `Error\n\n${message}`;
}

async function request(url, options = {}) {
  const res = await fetch(url, options);
  const contentType = res.headers.get("content-type") || "";
  let body = null;
  if (contentType.includes("application/json")) {
    body = await res.json();
  } else {
    body = await res.text();
  }
  if (!res.ok) {
    throw new Error(typeof body === "string" && body ? body : JSON.stringify(body));
  }
  return body;
}

function flattenTree(node, out = []) {
  if (!node) return out;
  out.push({ id: node.id, value: node.value });
  (node.children || []).forEach((c) => flattenTree(c, out));
  return out;
}

async function loadNodes() {
  try {
    const tree = await request("/tree");
    const nodes = flattenTree(tree, []);
    const options = nodes
      .map((n) => `<option value="${n.id}">${n.id} - ${escapeHtml(n.value)}</option>`)
      .join("");
    document.getElementById("parentSelect").innerHTML = options;
    document.getElementById("deleteSelect").innerHTML = options;
    document.getElementById("updateSelect").innerHTML = options;
  } catch (e) {
    // Árbol vacío o error; dejamos selectores vacíos
    document.getElementById("parentSelect").innerHTML = "";
    document.getElementById("deleteSelect").innerHTML = "";
    document.getElementById("updateSelect").innerHTML = "";
  }
}

function escapeHtml(s) {
  return String(s)
    .replaceAll("&", "&amp;")
    .replaceAll("<", "&lt;")
    .replaceAll(">", "&gt;")
    .replaceAll('"', "&quot;")
    .replaceAll("'", "&#039;");
}

async function createRoot() {
  const value = document.getElementById("rootValue").value.trim();
  if (!value) return showError("Ingresa un valor para la raíz taxonómica.");
  try {
    const data = await request(`/nodes/root?value=${encodeURIComponent(value)}`, {
      method: "POST",
    });
    show("Raíz taxonómica creada", data);
    document.getElementById("rootValue").value = "";
    await loadNodes();
  } catch (e) {
    showError(e.message);
  }
}

async function addChild() {
  const parentId = document.getElementById("parentSelect").value;
  const value = document.getElementById("childValue").value.trim();
  if (!parentId || !value) return showError("Selecciona un taxón padre e ingresa un valor.");
  try {
    const data = await request(
      `/nodes/${encodeURIComponent(parentId)}/children?value=${encodeURIComponent(value)}`,
      { method: "POST" }
    );
    show("Taxón hijo agregado", data);
    document.getElementById("childValue").value = "";
    await loadNodes();
  } catch (e) {
    showError(e.message);
  }
}

async function deleteNode() {
  const nodeId = document.getElementById("deleteSelect").value;
  if (!nodeId) return showError("Selecciona un taxón.");
  if (!confirm("¿Eliminar el taxón y todo su subárbol?")) return;
  try {
    await request(`/nodes/${encodeURIComponent(nodeId)}`, { method: "DELETE" });
    show("Taxón eliminado", { nodeId: Number(nodeId) });
    await loadNodes();
  } catch (e) {
    showError(e.message);
  }
}

async function updateNode() {
  const nodeId = document.getElementById("updateSelect").value;
  const value = document.getElementById("updateValue").value.trim();
  if (!nodeId || !value) return showError("Selecciona un taxón e ingresa un nuevo valor.");
  try {
    const ok = await request(
      `/nodes/${encodeURIComponent(nodeId)}?value=${encodeURIComponent(value)}`,
      { method: "PUT" }
    );
    show("Taxón actualizado", { ok });
    document.getElementById("updateValue").value = "";
    await loadNodes();
  } catch (e) {
    showError(e.message);
  }
}

async function getTree() {
  try {
    const data = await request("/tree");
    show("Árbol taxonómico completo", data);
  } catch (e) {
    showError(e.message);
  }
}

async function dfs() {
  try {
    const data = await request("/traversal/dfs");
    show("DFS", data);
  } catch (e) {
    showError(e.message);
  }
}

async function bfs() {
  try {
    const data = await request("/traversal/bfs");
    show("BFS", data);
  } catch (e) {
    showError(e.message);
  }
}

async function height() {
  try {
    const data = await request("/tree/height");
    show("Altura", { height: data });
  } catch (e) {
    showError(e.message);
  }
}

async function validate() {
  try {
    const data = await request("/tree/validate");
    show("Validación de ciclos", { valid: data });
  } catch (e) {
    showError(e.message);
  }
}

document.addEventListener("DOMContentLoaded", async () => {
  document.getElementById("createRootBtn").addEventListener("click", createRoot);
  document.getElementById("addChildBtn").addEventListener("click", addChild);
  document.getElementById("deleteBtn").addEventListener("click", deleteNode);
  document.getElementById("updateBtn").addEventListener("click", updateNode);
  document.getElementById("getTreeBtn").addEventListener("click", getTree);
  document.getElementById("dfsBtn").addEventListener("click", dfs);
  document.getElementById("bfsBtn").addEventListener("click", bfs);
  document.getElementById("heightBtn").addEventListener("click", height);
  document.getElementById("validateBtn").addEventListener("click", validate);
  await loadNodes();
});

