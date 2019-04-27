document.addEventListener("DOMContentLoaded", () => {
    const cl = document.body.classList;
    const table = $(document.getElementById("t"));
    const options = {
       ajax: (data, callback, oSettings) => $.ajax(
           {
               "cache": false,
               "data": data,
               "dataType": "json",
               "error": () => {
                   cl.remove('loading');
                   cl.add('auth');
               },
               "success": (json) => {
                   cl.remove('loading');
                   cl.remove('auth');
                   oSettings.json = json;
                   callback( json );
               },
               "type": oSettings.sServerMethod,
               "url": "/list",
           }
       ),
       serverSide: true,
    };
    table.DataTable(options);
});
