import { useKeycloak } from "@react-keycloak/web";

export default function Home() {
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
      }}>
      <div style={{ textAlign: "center", marginBottom: "150px" }}>
        <h1>Home</h1>
      </div>
      <div style={{ textAlign: "center" }}>
        <p>
          This is the home page. You can login or logout using the buttons
          below.
        </p>
        {keycloak?.authenticated ? (
          <button
            style={{
              borderRadius: "8px",
              backgroundColor: "#0087e0",
              color: "white",
              padding: "15px 20px",
              cursor: "pointer",
              border: "none",
            }}
            onClick={() => {
              keycloak.logout();
            }}>
            Logout
          </button>
        ) : (
          <button
            style={{
              borderRadius: "8px",
              backgroundColor: "#0087e0",
              color: "white",
              padding: "15px 20px",
              cursor: "pointer",
              border: "none",
            }}
            onClick={() => {
              keycloak.login();
            }}>
            Login
          </button>
        )}
        <p>
          After logging in, you can navigate to the dashboard by clicking the
          button below.
        </p>

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
          onClick={() => (window.location.href = "/dash")}>
          Go to Dashboard
        </button>
      </div>
    </div>
  );
}
