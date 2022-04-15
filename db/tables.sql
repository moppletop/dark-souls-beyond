-- Character Class Feature
create sequence character_class_feature_sequence;

create table character_class_feature
(
    id           int primary key,
    character_id int         not null,
    feature      varchar(48) not null
);

create index character_class_feature_character_id_idx on character_class_feature (character_id);

-- Character Class Feature Option
create sequence character_class_feature_option_sequence;

create table character_class_feature_option
(
    id         int primary key,
    feature_id int         not null,
    option     varchar(32) not null
);

create index character_class_feature_option_feature_id_idx on character_class_feature_option (feature_id);

-- Character
create sequence character_sequence;

create table character
(
    id                      int primary key,
    name                    varchar(32) not null,
    user_id                 int         not null,
    class                   int,
    origin                  int,
    level                   int,
    position                int,
    pre_combat_max_position int,
    position_dice           int         not null,
    souls_on_person         int         not null,
    souls_banked            int         not null
);

create index character_user_id_idx on character (user_id);

-- Character Inventory
create sequence character_inventory_sequence;

create table character_inventory
(
    id            int primary key,
    character_id  int         not null,
    item          varchar(32) not null,
    amount        int         not null,
    starting_item boolean     not null,
    active_slot   int
);

create index character_inventory_character_id_idx on character_inventory (character_id);

-- Character Uses
create table character_uses
(
    character_id int         not null,
    key          varchar(32) not null,
    uses         int         not null,

    primary key (character_id, key)
);

-- Character Spell
create sequence character_spell_sequence;

create table character_spell
(
    id           int primary key,
    character_id int         not null,
    spell        varchar(32) not null,
    attuned      boolean     not null,
    spent_casts  int         not null
);

create index character_spell_character_id_idx on character_spell (character_id);

-- User
create sequence dsb_user_sequence;

create table dsb_user
(
    id       int primary key,
    name     varchar(32) not null,
    password varchar(32) not null
);

create index user_name_idx on dsb_user (name);
