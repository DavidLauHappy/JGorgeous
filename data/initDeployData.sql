--部署工具的数据清理
delete from GROUP_VERSION;
delete from NODEDIR_MAP;
delete from NODE_FILES;
delete from VMP_CMDS;
delete from VMP_FILES;
delete from VMP_GROUP;
delete from VMP_NODE;
delete from VMP_STEPS;
delete from VMP_VERSION;
delete from DICTIONARY where TYPE not in('NODETYPE','NODECATE','VMP_CMDS.STATUS');
delete from PARAMETERS where NAME not in('LANGUAGE','DEBUG','KEY','SERVER_PORT','FTP_PORT','Refresh_Frequency','Input_Incluede','Max_Connection','LICENSE','Expired_Date','Time_Span','Auto_BackupDb','AllNode_upload');
UPDATE PARAMETERS SET VALUE='' WHERE NAME='LICENSE';
UPDATE PARAMETERS SET VALUE='' WHERE NAME='Expired_Date';