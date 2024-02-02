alter table if exists users
    alter column balance type float4 using balance::float4;