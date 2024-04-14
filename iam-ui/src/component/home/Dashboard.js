import { useKeycloak } from "@react-keycloak/web";

export default function Dashboard() {
  const { keycloak } = useKeycloak();

  return (
    <div
      style={{
        margin: 0,
        width: "100%",
        height: "100%",
        display: "flex",
        flexGrow: 1,
        flexDirection: "column",
        alignItems: "center",
        justifyContent: "space-around",
      }}>
      <div style={{ textAlign: "center" }}>
        <h1>Dashboard</h1>
        <h3 style={{ marginTop: "20px" }}>
          Hello{" "}
          <i>
            <b>{keycloak?.tokenParsed?.preferred_username}</b>
          </i>
        </h3>
      </div>

      <div
        style={{
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
        }}>
        <h3>Your token is:</h3>
        <div
          style={{
            padding: "7px",
            backgroundColor: "white",
            borderRadius: "8px",
            width: "50%",
            height: "50%",
            display: "flex",
            flexWrap: "wrap",
            overflow: "scroll",
          }}>
          <p style={{ wordBreak: "break-all" }}>{keycloak.token.toString()}</p>
        </div>
        <div
          style={{
            display: "flex",
            alignItems: "center",
            marginTop: "20px",
            marginBottom: "20px",
          }}>
          <button
            style={{
              borderRadius: "8px",
              backgroundColor: "#0087e0",
              color: "white",
              padding: "15px 20px",
              cursor: "pointer",
              border: "none",
            }}
            onClick={() => navigator.clipboard.writeText(keycloak?.token)}>
            Copy to clipboard
          </button>
          <br />
          {keycloak?.authenticated ? (
            <button
              style={{
                borderRadius: "8px",
                backgroundColor: "#21d5b0",
                color: "white",
                padding: "15px 20px",
                cursor: "pointer",
                marginLeft: "10px",
                border: "none",
              }}
              onClick={() => {
                keycloak.logout();
              }}>
              Logout
            </button>
          ) : (
            <button
              onClick={() => {
                keycloak.login();
              }}
              style={{
                borderRadius: "8px",
                backgroundColor: "#21d5b0",
                color: "white",
                padding: "15px 20px",
                cursor: "pointer",
                marginLeft: "10px",
                border: "none",
              }}>
              Login
            </button>
          )}
        </div>
      </div>
    </div>
  );
}
