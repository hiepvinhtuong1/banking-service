export const mockAccounts = [
    {
        accountId: 1,
        customerName: "Nguyen Van A",
        email: "vana@example.com",
        phoneNumber: "0912345678",
        roles: [
            {
                name: "ROLE_ADMIN",
                permissions: [
                    { name: "READ_USER" },
                    { name: "WRITE_USER" },
                    { name: "DELETE_USER" },
                ],
            },
        ],
        active: true,
    },
    {
        accountId: 2,
        customerName: "Tran Thi B",
        email: "thib@example.com",
        phoneNumber: "0987654321",
        roles: [
            {
                name: "ROLE_USER",
                permissions: [{ name: "READ_USER" }],
            },
        ],
        active: false,
    },
    {
        accountId: 3,
        customerName: "Le Van C",
        email: "cvan@example.com",
        phoneNumber: "0909090909",
        roles: [
            {
                name: "ROLE_MANAGER",
                permissions: [
                    { name: "READ_USER" },
                    { name: "WRITE_USER" },
                ],
            },
        ],
        active: true,
    },
    {
        accountId: 4,
        customerName: "Pham Thi D",
        email: "dthi@example.com",
        phoneNumber: "0934567890",
        roles: [
            {
                name: "ROLE_USER",
                permissions: [{ name: "READ_USER" }],
            },
        ],
        active: true,
    },
    {
        accountId: 5,
        customerName: "Hoang Van E",
        email: "ehoang@example.com",
        phoneNumber: "0978123456",
        roles: [
            {
                name: "ROLE_ADMIN",
                permissions: [
                    { name: "READ_USER" },
                    { name: "WRITE_USER" },
                    { name: "DELETE_USER" },
                ],
            },
        ],
        active: false,
    },
    {
        accountId: 6,
        customerName: "Dang Thi F",
        email: "fdang@example.com",
        phoneNumber: "0923456789",
        roles: [
            {
                name: "ROLE_USER",
                permissions: [{ name: "READ_USER" }],
            },
        ],
        active: true,
    },
    {
        accountId: 7,
        customerName: "Vu Van G",
        email: "gvu@example.com",
        phoneNumber: "0911222333",
        roles: [
            {
                name: "ROLE_MANAGER",
                permissions: [
                    { name: "READ_USER" },
                    { name: "WRITE_USER" },
                ],
            },
        ],
        active: false,
    },
    {
        accountId: 8,
        customerName: "Bui Thi H",
        email: "hbui@example.com",
        phoneNumber: "0944556677",
        roles: [
            {
                name: "ROLE_USER",
                permissions: [{ name: "READ_USER" }],
            },
        ],
        active: true,
    },
    {
        accountId: 9,
        customerName: "Do Van I",
        email: "ido@example.com",
        phoneNumber: "0966778899",
        roles: [
            {
                name: "ROLE_ADMIN",
                permissions: [
                    { name: "READ_USER" },
                    { name: "WRITE_USER" },
                    { name: "DELETE_USER" },
                ],
            },
        ],
        active: true,
    },
    {
        accountId: 10,
        customerName: "Nguyen Thi J",
        email: "jnguyen@example.com",
        phoneNumber: "0955667788",
        roles: [
            {
                name: "ROLE_USER",
                permissions: [{ name: "READ_USER" }],
            },
        ],
        active: false,
    },
];
