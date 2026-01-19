import React, { useState, useEffect } from 'react';
import {
    Box,
    Typography,
    Paper,
    TextField,
    Button,
    Avatar,
    Grid,
    Stack,
    IconButton,
    Alert,
    Snackbar,
    Divider,
    Card,
    CardContent,
    Chip
} from '@mui/material';
import {
    Edit as EditIcon,
    Save as SaveIcon,
    Cancel as CancelIcon,
    Person as PersonIcon,
    Email as EmailIcon,
    School as SchoolIcon,
    Badge as BadgeIcon,
    PhotoCamera as PhotoCameraIcon
} from '@mui/icons-material';
import { useAuth } from './context/AuthContext';
import { alpha } from '@mui/material/styles';

const ProfilePage = () => {
    const { user, updateUser } = useAuth();
    const [isEditing, setIsEditing] = useState(false);
    const [snackbar, setSnackbar] = useState({ open: false, message: '', severity: 'success' });

    const [formData, setFormData] = useState({
        username: user?.username || '',
        email: user?.email || '',
        fullName: user?.fullName || user?.username || '',
        phone: user?.phone || '',
        bio: user?.bio || '',
        department: user?.department || '',
        studentId: user?.studentId || user?.userId || ''
    });

    useEffect(() => {
        if (user) {
            setFormData({
                username: user.username || '',
                email: user.email || '',
                fullName: user.fullName || user.username || '',
                phone: user.phone || '',
                bio: user.bio || '',
                department: user.department || '',
                studentId: user.studentId || user.userId || ''
            });
        }
    }, [user]);

    const handleChange = (field) => (e) => {
        setFormData({ ...formData, [field]: e.target.value });
    };

    const handleSave = () => {
        // Update user in context and localStorage
        updateUser({
            ...formData,
            fullName: formData.fullName
        });
        setIsEditing(false);
        setSnackbar({ open: true, message: 'Profile updated successfully!', severity: 'success' });
    };

    const handleCancel = () => {
        setFormData({
            username: user?.username || '',
            email: user?.email || '',
            fullName: user?.fullName || user?.username || '',
            phone: user?.phone || '',
            bio: user?.bio || '',
            department: user?.department || '',
            studentId: user?.studentId || user?.userId || ''
        });
        setIsEditing(false);
    };

    const getInitials = (name) => {
        if (!name) return 'U';
        return name.split(' ').map(n => n[0]).join('').toUpperCase().slice(0, 2);
    };

    const isStudent = user?.role === 'STUDENT';

    return (
        <Box sx={{ minHeight: '100vh', p: { xs: 2, md: 4 } }}>
            <Box sx={{ maxWidth: 900, mx: 'auto' }}>
                {/* Header */}
                <Box sx={{ mb: 4 }}>
                    <Typography variant="h4" sx={{ fontWeight: 900, color: 'text.primary', mb: 1 }}>
                        My Profile
                    </Typography>
                    <Typography variant="body2" color="text.secondary">
                        Manage your personal information and account settings
                    </Typography>
                </Box>

                {/* Profile Card */}
                <Paper sx={{
                    borderRadius: 4,
                    overflow: 'hidden',
                    boxShadow: '0 10px 40px rgba(0,0,0,0.08)',
                    mb: 4
                }}>
                    {/* Banner */}
                    <Box sx={{
                        height: 120,
                        background: 'linear-gradient(135deg, #6366f1 0%, #8b5cf6 50%, #a855f7 100%)',
                        position: 'relative'
                    }} />

                    {/* Profile Info */}
                    <Box sx={{ p: 4, pt: 0 }}>
                        <Box sx={{ display: 'flex', alignItems: 'flex-end', gap: 3, mt: -8, mb: 3, flexWrap: 'wrap' }}>
                            <Box sx={{ position: 'relative' }}>
                                <Avatar
                                    sx={{
                                        width: 120,
                                        height: 120,
                                        fontSize: '2.5rem',
                                        fontWeight: 800,
                                        bgcolor: '#1e293b',
                                        border: '4px solid white',
                                        boxShadow: '0 4px 20px rgba(0,0,0,0.15)'
                                    }}
                                >
                                    {getInitials(formData.fullName)}
                                </Avatar>
                                {isEditing && (
                                    <IconButton
                                        size="small"
                                        sx={{
                                            position: 'absolute',
                                            bottom: 0,
                                            right: 0,
                                            bgcolor: 'primary.main',
                                            color: 'white',
                                            '&:hover': { bgcolor: 'primary.dark' }
                                        }}
                                    >
                                        <PhotoCameraIcon fontSize="small" />
                                    </IconButton>
                                )}
                            </Box>
                            <Box sx={{ flex: 1, mb: 2 }}>
                                <Typography variant="h5" sx={{ fontWeight: 800, color: 'text.primary' }}>
                                    {formData.fullName || 'Your Name'}
                                </Typography>
                                <Stack direction="row" spacing={1} alignItems="center" sx={{ mt: 1 }}>
                                    <Chip
                                        label={isStudent ? 'Student' : 'Instructor'}
                                        size="small"
                                        sx={{
                                            bgcolor: isStudent ? alpha('#10b981', 0.1) : alpha('#6366f1', 0.1),
                                            color: isStudent ? '#10b981' : '#6366f1',
                                            fontWeight: 700
                                        }}
                                    />
                                    {formData.department && (
                                        <Chip
                                            label={formData.department}
                                            size="small"
                                            variant="outlined"
                                            sx={{ fontWeight: 600 }}
                                        />
                                    )}
                                </Stack>
                            </Box>
                            <Box>
                                {!isEditing ? (
                                    <Button
                                        variant="contained"
                                        startIcon={<EditIcon />}
                                        onClick={() => setIsEditing(true)}
                                        sx={{
                                            borderRadius: 3,
                                            px: 3,
                                            fontWeight: 700,
                                            textTransform: 'none'
                                        }}
                                    >
                                        Edit Profile
                                    </Button>
                                ) : (
                                    <Stack direction="row" spacing={2}>
                                        <Button
                                            variant="outlined"
                                            startIcon={<CancelIcon />}
                                            onClick={handleCancel}
                                            sx={{ borderRadius: 3, fontWeight: 700, textTransform: 'none' }}
                                        >
                                            Cancel
                                        </Button>
                                        <Button
                                            variant="contained"
                                            startIcon={<SaveIcon />}
                                            onClick={handleSave}
                                            sx={{ borderRadius: 3, fontWeight: 700, textTransform: 'none' }}
                                        >
                                            Save Changes
                                        </Button>
                                    </Stack>
                                )}
                            </Box>
                        </Box>

                        <Divider sx={{ my: 3 }} />

                        {/* Form Fields */}
                        <Grid container spacing={3}>
                            <Grid item xs={12} md={6}>
                                <TextField
                                    fullWidth
                                    label="Full Name"
                                    value={formData.fullName}
                                    onChange={handleChange('fullName')}
                                    disabled={!isEditing}
                                    InputProps={{
                                        startAdornment: <PersonIcon sx={{ mr: 1, color: 'text.secondary' }} />
                                    }}
                                    sx={{
                                        '& .MuiOutlinedInput-root': {
                                            borderRadius: 2,
                                            bgcolor: isEditing ? 'background.paper' : 'action.hover'
                                        }
                                    }}
                                />
                            </Grid>
                            <Grid item xs={12} md={6}>
                                <TextField
                                    fullWidth
                                    label="Username"
                                    value={formData.username}
                                    disabled
                                    InputProps={{
                                        startAdornment: <BadgeIcon sx={{ mr: 1, color: 'text.secondary' }} />
                                    }}
                                    sx={{
                                        '& .MuiOutlinedInput-root': {
                                            borderRadius: 2,
                                            bgcolor: 'action.hover'
                                        }
                                    }}
                                />
                            </Grid>
                            <Grid item xs={12} md={6}>
                                <TextField
                                    fullWidth
                                    label="Email"
                                    value={formData.email}
                                    onChange={handleChange('email')}
                                    disabled={!isEditing}
                                    InputProps={{
                                        startAdornment: <EmailIcon sx={{ mr: 1, color: 'text.secondary' }} />
                                    }}
                                    sx={{
                                        '& .MuiOutlinedInput-root': {
                                            borderRadius: 2,
                                            bgcolor: isEditing ? 'background.paper' : 'action.hover'
                                        }
                                    }}
                                />
                            </Grid>
                            <Grid item xs={12} md={6}>
                                <TextField
                                    fullWidth
                                    label={isStudent ? "Student ID" : "Instructor ID"}
                                    value={formData.studentId}
                                    disabled
                                    InputProps={{
                                        startAdornment: <SchoolIcon sx={{ mr: 1, color: 'text.secondary' }} />
                                    }}
                                    sx={{
                                        '& .MuiOutlinedInput-root': {
                                            borderRadius: 2,
                                            bgcolor: 'action.hover'
                                        }
                                    }}
                                />
                            </Grid>
                            <Grid item xs={12} md={6}>
                                <TextField
                                    fullWidth
                                    label="Department"
                                    value={formData.department}
                                    onChange={handleChange('department')}
                                    disabled={!isEditing}
                                    placeholder="e.g., Computer Science"
                                    sx={{
                                        '& .MuiOutlinedInput-root': {
                                            borderRadius: 2,
                                            bgcolor: isEditing ? 'background.paper' : 'action.hover'
                                        }
                                    }}
                                />
                            </Grid>
                            <Grid item xs={12} md={6}>
                                <TextField
                                    fullWidth
                                    label="Phone"
                                    value={formData.phone}
                                    onChange={handleChange('phone')}
                                    disabled={!isEditing}
                                    placeholder="+1 234 567 8900"
                                    sx={{
                                        '& .MuiOutlinedInput-root': {
                                            borderRadius: 2,
                                            bgcolor: isEditing ? 'background.paper' : 'action.hover'
                                        }
                                    }}
                                />
                            </Grid>
                            <Grid item xs={12}>
                                <TextField
                                    fullWidth
                                    label="Bio"
                                    value={formData.bio}
                                    onChange={handleChange('bio')}
                                    disabled={!isEditing}
                                    multiline
                                    rows={3}
                                    placeholder="Tell us about yourself..."
                                    sx={{
                                        '& .MuiOutlinedInput-root': {
                                            borderRadius: 2,
                                            bgcolor: isEditing ? 'background.paper' : 'action.hover'
                                        }
                                    }}
                                />
                            </Grid>
                        </Grid>
                    </Box>
                </Paper>

                {/* Stats Cards */}
                <Grid container spacing={3}>
                    <Grid item xs={12} md={4}>
                        <Card sx={{ borderRadius: 3, height: '100%' }}>
                            <CardContent sx={{ p: 3 }}>
                                <Typography variant="overline" color="text.secondary" fontWeight={700}>
                                    Account Status
                                </Typography>
                                <Stack direction="row" alignItems="center" spacing={1} sx={{ mt: 1 }}>
                                    <Box sx={{ width: 8, height: 8, borderRadius: '50%', bgcolor: '#10b981' }} />
                                    <Typography variant="h6" fontWeight={800} color="#10b981">
                                        Active
                                    </Typography>
                                </Stack>
                            </CardContent>
                        </Card>
                    </Grid>
                    <Grid item xs={12} md={4}>
                        <Card sx={{ borderRadius: 3, height: '100%' }}>
                            <CardContent sx={{ p: 3 }}>
                                <Typography variant="overline" color="text.secondary" fontWeight={700}>
                                    Role
                                </Typography>
                                <Typography variant="h6" fontWeight={800} color="primary.main" sx={{ mt: 1 }}>
                                    {isStudent ? 'Student' : 'Instructor'}
                                </Typography>
                            </CardContent>
                        </Card>
                    </Grid>
                    <Grid item xs={12} md={4}>
                        <Card sx={{ borderRadius: 3, height: '100%' }}>
                            <CardContent sx={{ p: 3 }}>
                                <Typography variant="overline" color="text.secondary" fontWeight={700}>
                                    Member Since
                                </Typography>
                                <Typography variant="h6" fontWeight={800} sx={{ mt: 1 }}>
                                    {new Date().toLocaleDateString('en-US', { month: 'short', year: 'numeric' })}
                                </Typography>
                            </CardContent>
                        </Card>
                    </Grid>
                </Grid>
            </Box>

            <Snackbar
                open={snackbar.open}
                autoHideDuration={4000}
                onClose={() => setSnackbar({ ...snackbar, open: false })}
                anchorOrigin={{ vertical: 'bottom', horizontal: 'center' }}
            >
                <Alert
                    onClose={() => setSnackbar({ ...snackbar, open: false })}
                    severity={snackbar.severity}
                    sx={{ borderRadius: 2 }}
                >
                    {snackbar.message}
                </Alert>
            </Snackbar>
        </Box>
    );
};

export default ProfilePage;
