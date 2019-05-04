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
					callback(json);
				},
				"type": oSettings.sServerMethod,
				"url": "/list",
			}
		),
		serverSide: true,
	};
	const dt = table.DataTable(options);

	const filter = (element, column_index) => {
		const $e = $(element);
		$e.typeWatch({
			callback: () => dt.column(column_index)
				.search($e.val())
				.draw(),
			captureLength: 0,
			wait: 350,
		});
	};

	filter(document.getElementById("fn_filter"), 0);
	filter(document.getElementById("ln_filter"), 1);
});
