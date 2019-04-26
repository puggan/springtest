document.addEventListener("DOMContentLoaded", () => {
    const table = $(document.getElementById("t"));
    const options = {
       ajax: "/list",
       serverSide: true,
    };
    table.DataTable(options);
});