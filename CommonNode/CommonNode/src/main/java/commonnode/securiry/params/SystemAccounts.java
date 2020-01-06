package commonnode.securiry.params;

import lombok.Getter;

public enum SystemAccounts {
	ADMIN {
		@Getter
		private int id = -10;
		@Getter
		private String login = "Admin@devShop.com";
		@Getter
		private String password = "7a9f39cbf01681af1becf1634cbc40e090f8715b856ec82fba81226f538f7e26bff80b67bf919fad11c3eb1a80c9dc7f252d9bfea3e125a1bea96d3dac152ccf";
		@Getter
		private AuthRoles role = AuthRoles.ADMIN;

		@Override
		public String getDecryptPassword() {
			return "1123";
		}
	},
	SERVICE{
		@Getter
		private int id = -10;
		@Getter
		private String login = "service@devShop.com";
		@Getter
		private String password = "7a9f39cbf01681af1becf1634cbc40e090f8715b856ec82fba81226f538f7e26bff80b67bf919fad11c3eb1a80c9dc7f252d9bfea3e125a1bea96d3dac152ccf";
		@Getter
		private AuthRoles role = AuthRoles.SERVICE;

		@Override
		public String getDecryptPassword() {
			return "1123";
		}
	},
	OFFER_NODE {
		@Getter
		private int id = -11;
		@Getter
		private String login = "OfferNode@devShop.com";
		@Getter
		private String password = "7a9f39cbf01681af1becf1634cbc40e090f8715b856ec82fba81226f538f7e26bff80b67bf919fad11c3eb1a80c9dc7f252d9bfea3e125a1bea96d3dac152ccf";
		@Getter
		private AuthRoles role = AuthRoles.SERVICE;

		@Override
		public String getDecryptPassword() {
			return "1123";
		}
	},
	ORDERNODE {
		@Getter
		private int id = -12;
		@Getter
		private String login = "OrderNode@devShop.com";
		@Getter
		private String password = "7a9f39cbf01681af1becf1634cbc40e090f8715b856ec82fba81226f538f7e26bff80b67bf919fad11c3eb1a80c9dc7f252d9bfea3e125a1bea96d3dac152ccf";
		@Getter
		private AuthRoles role = AuthRoles.SERVICE;

		@Override
		public String getDecryptPassword() {
			return "1123";
		}
	};

	public abstract int getId();

	public abstract String getLogin();

	public abstract String getPassword();

	public abstract String getDecryptPassword();

	public abstract AuthRoles getRole();
}
